#!/usr/bin/env python3
import os
import pickle
import psycopg2
from psycopg2 import sql
from flask import Flask, request, jsonify
import pandas as pd
from flask_cors import CORS
import logging

# Configure basic logging.
logging.basicConfig(level=logging.INFO)

app = Flask(__name__)
# Allow CORS for all endpoints (adjust origins as needed)
CORS(app)

# Postgres configuration
DB_HOST = os.getenv('DB_HOST', 'localhost')
DB_NAME = os.getenv('DB_NAME', 'myappdb')
DB_USER = os.getenv('DB_USER', 'postgres')
DB_PASS = os.getenv('DB_PASS', 'root')
DB_PORT = os.getenv('DB_PORT', '5432')

def get_db_connection():
    return psycopg2.connect(
        host=DB_HOST,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASS,
        port=DB_PORT
    )

#############################################################################
# 1. LOAD TRAINED MODELS
#############################################################################
BASE_DIR = os.path.dirname(__file__)
CLASSIFIER_PATH = os.path.join(BASE_DIR, "models", "best_classifier.pkl")
LIMIT_REGRESSOR_PATH = os.path.join(BASE_DIR, "models", "best_regression_limit.pkl")
INTEREST_REGRESSOR_PATH = os.path.join(BASE_DIR, "models", "best_regression_interest.pkl")

with open(CLASSIFIER_PATH, "rb") as f:
    best_classifier = pickle.load(f)
with open(LIMIT_REGRESSOR_PATH, "rb") as f:
    best_regressor_limit = pickle.load(f)
with open(INTEREST_REGRESSOR_PATH, "rb") as f:
    best_regressor_interest = pickle.load(f)

#############################################################################
# 2. CREATE TABLE IF NOT EXISTS
#############################################################################
def create_table_if_not_exists():
    create_query = """
    CREATE TABLE IF NOT EXISTS applications (
        applicant_id VARCHAR(255) PRIMARY KEY,
        self_reported_expenses FLOAT,
        credit_score INT,
        annual_income FLOAT,
        self_reported_debt FLOAT,
        requested_amount FLOAT,
        age INT,
        province VARCHAR(10),
        employment_status VARCHAR(20),
        months_employed INT,
        credit_utilization FLOAT,
        num_open_accounts INT,
        num_credit_inquiries INT,
        payment_history VARCHAR(20),
        current_credit_limit FLOAT,
        monthly_expenses FLOAT,
        approved INT,
        approved_amount FLOAT,
        estimated_debt FLOAT,
        interest_rate FLOAT,
        dti FLOAT
    );
    """
    try:
        conn = get_db_connection()
        cur = conn.cursor()
        cur.execute(create_query)
        conn.commit()
        cur.close()
        conn.close()
        logging.info("Table 'applications' ensured to exist.")
    except Exception as e:
        logging.error("Error creating table: %s", e)

create_table_if_not_exists()

#############################################################################
# 3. HELPER FUNCTIONS FOR FEATURE CALCULATIONS
#############################################################################
def compute_estimated_debt(data):
    """
    Computes estimated_debt using the rule:
      estimated_debt = current_credit_limit * (credit_utilization/100) * 0.03
    """
    try:
        current_credit_limit = float(data.get("current_credit_limit", 0.0))
        credit_utilization = float(data.get("credit_utilization", 0.0))
        return current_credit_limit * (credit_utilization / 100.0) * 0.03
    except Exception as e:
        logging.error("Error computing estimated debt: %s", e)
        return 0.0

def compute_dti(data, estimated_debt):
    """
    Computes DTI (debt-to-income ratio) using:
      DTI = (self_reported_debt + estimated_debt + (requested_amount * 0.03))
            / (annual_income / 12)
    """
    try:
        annual_income = float(data.get("annual_income", 1.0))
        self_reported_debt = float(data.get("self_reported_debt", 0.0))
        requested_amount = float(data.get("requested_amount", 0.0))
        monthly_income = annual_income / 12.0
        return (self_reported_debt + estimated_debt + (requested_amount * 0.03)) / monthly_income
    except Exception as e:
        logging.error("Error computing DTI: %s", e)
        return 0.0

#############################################################################
# 4. PREDICTION ENDPOINT
#############################################################################
@app.route('/predict', methods=['POST'])
def predict_loc():
    data = request.get_json()
    if not data:
        return jsonify({"error": "Invalid input data"}), 400

    # Use provided values if available; otherwise compute.
    estimated_debt = (float(data["estimated_debt"]) if "estimated_debt" in data and data["estimated_debt"] is not None 
                      else compute_estimated_debt(data))
    dti_value = (float(data["dti"]) if "dti" in data and data["dti"] is not None 
                 else compute_dti(data, estimated_debt))

    # Build input features dataframe (note the key "DTI" should match the model training schema)
    input_features = {
        "self_reported_expenses": float(data.get("self_reported_expenses", 0.0)),
        "annual_income": float(data.get("annual_income", 0.0)),
        "self_reported_debt": float(data.get("self_reported_debt", 0.0)),
        "requested_amount": float(data.get("requested_amount", 0.0)),
        "age": int(data.get("age", 0)),
        "province": data.get("province", "ON"),
        "employment_status": data.get("employment_status", "Unemployed"),
        "months_employed": int(data.get("months_employed", 0)),
        "credit_score": int(data.get("credit_score", 300)),
        "credit_utilization": float(data.get("credit_utilization", 0.0)),
        "num_open_accounts": int(data.get("num_open_accounts", 0)),
        "num_credit_inquiries": int(data.get("num_credit_inquiries", 0)),
        "payment_history": data.get("payment_history", "On Time"),
        "estimated_debt": estimated_debt,
        "DTI": dti_value,
        "monthly_expenses": float(data.get("monthly_expenses", 0.0)),
        "current_credit_limit": float(data.get("current_credit_limit", 0.0))
    }
    input_df = pd.DataFrame([input_features])

    try:
        # Get predictions from the three models.
        approved_pred = best_classifier.predict(input_df)[0]
        limit_pred = best_regressor_limit.predict(input_df)[0]
        interest_pred = best_regressor_interest.predict(input_df)[0]
    except Exception as e:
        logging.error("Error during model prediction: %s", e)
        return jsonify({"error": "Prediction failed"}), 500

    if approved_pred == 0:
        final_limit = 0.0
        final_rate = 0.0
    else:
        final_limit = float(limit_pred)
        final_rate = round(float(interest_pred), 2)

    results = {
        "approved": int(approved_pred),
        "approved_amount": final_limit,
        "interest_rate": final_rate
    }

    # Prepare the insert query and parameters.
    insert_query = """
    INSERT INTO applications (
        applicant_id,
        self_reported_expenses,
        credit_score,
        annual_income,
        self_reported_debt,
        requested_amount,
        age,
        province,
        employment_status,
        months_employed,
        credit_utilization,
        num_open_accounts,
        num_credit_inquiries,
        payment_history,
        current_credit_limit,
        monthly_expenses,
        approved,
        approved_amount,
        estimated_debt,
        interest_rate,
        dti
    )
    VALUES (
        %(applicant_id)s,
        %(self_reported_expenses)s,
        %(credit_score)s,
        %(annual_income)s,
        %(self_reported_debt)s,
        %(requested_amount)s,
        %(age)s,
        %(province)s,
        %(employment_status)s,
        %(months_employed)s,
        %(credit_utilization)s,
        %(num_open_accounts)s,
        %(num_credit_inquiries)s,
        %(payment_history)s,
        %(current_credit_limit)s,
        %(monthly_expenses)s,
        %(approved)s,
        %(approved_amount)s,
        %(estimated_debt)s,
        %(interest_rate)s,
        %(dti)s
    )
    ON CONFLICT (applicant_id) DO UPDATE SET
        self_reported_expenses = EXCLUDED.self_reported_expenses,
        credit_score = EXCLUDED.credit_score,
        annual_income = EXCLUDED.annual_income,
        self_reported_debt = EXCLUDED.self_reported_debt,
        requested_amount = EXCLUDED.requested_amount,
        age = EXCLUDED.age,
        province = EXCLUDED.province,
        employment_status = EXCLUDED.employment_status,
        months_employed = EXCLUDED.months_employed,
        credit_utilization = EXCLUDED.credit_utilization,
        num_open_accounts = EXCLUDED.num_open_accounts,
        num_credit_inquiries = EXCLUDED.num_credit_inquiries,
        payment_history = EXCLUDED.payment_history,
        current_credit_limit = EXCLUDED.current_credit_limit,
        monthly_expenses = EXCLUDED.monthly_expenses,
        approved = EXCLUDED.approved,
        approved_amount = EXCLUDED.approved_amount,
        estimated_debt = EXCLUDED.estimated_debt,
        interest_rate = EXCLUDED.interest_rate,
        dti = EXCLUDED.dti
    """
    parameters = {
        "applicant_id": data.get("applicant_id", "NA"),
        "self_reported_expenses": input_features["self_reported_expenses"],
        "credit_score": input_features["credit_score"],
        "annual_income": input_features["annual_income"],
        "self_reported_debt": input_features["self_reported_debt"],
        "requested_amount": input_features["requested_amount"],
        "age": input_features["age"],
        "province": input_features["province"],
        "employment_status": input_features["employment_status"],
        "months_employed": input_features["months_employed"],
        "credit_utilization": input_features["credit_utilization"],
        "num_open_accounts": input_features["num_open_accounts"],
        "num_credit_inquiries": input_features["num_credit_inquiries"],
        "payment_history": input_features["payment_history"],
        "current_credit_limit": input_features["current_credit_limit"],
        "monthly_expenses": input_features["monthly_expenses"],
        "approved": int(approved_pred),
        "approved_amount": float(final_limit),
        "estimated_debt": estimated_debt,
        "interest_rate": float(final_rate),
        "dti": dti_value
    }
    try:
        conn = get_db_connection()
        cur = conn.cursor()
        cur.execute(insert_query, parameters)
        conn.commit()
        cur.close()
        conn.close()
    except Exception as e:
        logging.error("Database insert error: %s", e)
        return jsonify({"error": "Database operation failed"}), 500

    return jsonify(results), 200

#############################################################################
# 5. HEALTH ENDPOINT
#############################################################################
@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "OK"}), 200

#############################################################################
# 6. MAIN ENTRY POINT
#############################################################################
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5050)