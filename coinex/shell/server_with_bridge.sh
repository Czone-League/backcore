#!/bin/sh
#
# Copyright 2014 Coinport Inc. All Rights Reserved.
# Author: c@coinport.com (Chao Ma)

sbt "project coinex-backend" "run 25552 127.0.0.1:25551 * 127.0.0.1"