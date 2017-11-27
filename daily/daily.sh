#!/bin/bash

# Run this script with no arguments to run the frontend and backend for a day's worth of inputs.
# Expects to be run when current directory is the same directory that script is in.
# Reads from the files in the inputs folder.

mkdir -p transaction_summaries
rm transaction_summaries/*
rm merged

for INPUT in inputs/*; do
    java -cp ../frontend/bin abl_cisc327.Main valid_accounts transaction_summaries/$(basename "$INPUT") < $INPUT
done

cat transaction_summaries/* > merged

java -cp ../backend/bin backend.Main merged master_accounts master_accounts valid_accounts