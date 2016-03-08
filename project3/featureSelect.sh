#!/bin/bash
#Author: Yihong Zhou (yzhou8)
#			Mengwen Li (mli2)

if (($# != 2))
then
	echo "Usage: ./featureSelect.sh <input file name> <output file name>"
	exit 1
fi
java -jar project3_featureSelect.jar $1 $2
