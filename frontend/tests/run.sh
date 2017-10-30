#!/bin/bash

RED="\033[0;31m"
GREEN="\033[0;32m"
YELLOW="\033[0;33m"
RESET="\033[0;0m"

rm -r output
for folder in $(ls -v input); do
	mkdir -p output/$folder
	echo -e "${RESET}Running test ${YELLOW}$folder${RESET}"
	java -cp ../bin/ abl_cisc327.Main input/$folder/valid_accounts.txt output/$folder/transaction_summary.log < input/$folder/in.txt > output/$folder/out.log

	diff -q expected/$folder/out.txt output/$folder/out.log
	OUTPUT_CODE=$?

	if [ -f expected/$folder/transaction_summary.txt ]; then
		diff -q expected/$folder/transaction_summary.txt output/$folder/transaction_summary.log
		TRANSACTION_CODE=$?
	else
		TRANSACTION_CODE=0
	fi

	if [ $TRANSACTION_CODE == 0 ] && [ $OUTPUT_CODE == 0 ]; then
		echo -e "${RESET} - ${GREEN}Passed"
	fi
done
