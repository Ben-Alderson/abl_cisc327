#!/bin/bash

cd cases
for folder in $(ls | sort -n); do
	echo "Running test $folder"
	java -cp ../../bin/ abl_cisc327.Main $folder/valid_accounts.txt $folder/valid_accounts.log < $folder/in.txt > $folder/out.log
done
