#!/bin/bash

rm transaction_summaries/*
rm merged

for INPUT in inputs/*; do
    java -cp ../frontend/bin abl_cisc327.Main valid_accounts transaction_summaries/$(basename "$INPUT") < $INPUT
done

cat transaction_summaries/* > merged

java -cp ../backend/bin backend.Main merged master_accounts master_accounts valid_accounts