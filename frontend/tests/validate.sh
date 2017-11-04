#!/bin/bash

RED="\033[0;31m"
GREEN="\033[0;32m"
YELLOW="\033[0;33m"
RESET="\033[0;0m"

THERE_WERE_PROBLEMS=0
echo

for folder in $(ls -v input); do
    # Check the terminal output
    diff -s expected/$folder/out.txt output/$folder/out.log > /dev/null
    OUTPUT_CODE=$?

    # Print out the diff if it failed
    if [ $OUTPUT_CODE != 0 ]; then
        THERE_WERE_PROBLEMS=1
        echo -e "${YELLOW}$folder${RESET} - Terminal output didn't match: ${RED}"
        diff expected/$folder/out.txt output/$folder/out.log
        echo -ne "${RESET}"
        echo
    fi

    # Check that we actually have a file
    if [ -f expected/$folder/transaction_summary.txt ]; then
        diff -s expected/$folder/transaction_summary.txt output/$folder/transaction_summary.log > /dev/null
        TRANSACTION_CODE=$?
    else
        TRANSACTION_CODE=0
    fi

    if [ $TRANSACTION_CODE != 0 ]; then
        THERE_WERE_PROBLEMS=1
        echo -e "${YELLOW}$folder${RESET} - Transaction summary didn't match: ${RED}"
        diff expected/$folder/transaction_summary.txt output/$folder/transaction_summary.log
        echo -ne "${RESET}"
        echo
    fi
done

if [ $THERE_WERE_PROBLEMS == 0 ]; then
    echo -e "${GREEN}All tests passed${RESET}"
else
    echo -e "Some tests didn't pass${RESET}"
fi