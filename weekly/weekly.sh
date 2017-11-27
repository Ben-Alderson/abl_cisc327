#!/bin/bash

# Run this script with no arguments to run a week of transactions.
# Expects to be run when current directory is the same directory that script is in.
# Expects the daily script to be in an adjacent directory.
# The repository's layout satisfies that requirement.

START_DIR=$(pwd)
rm ../daily/master_accounts
rm ../daily/valid_accounts
echo "0000000" > ../daily/valid_accounts
touch ../daily/master_accounts
for DAY in days/*; do
    rm -r ../daily/inputs

    cp -r $DAY ../daily/inputs

    cd ../daily
    ./daily.sh
    cd $START_DIR
    read
done