# Introduction

Simple extension to allow writing custom metrics to AppDynamics

[![Build Status](https://travis-ci.org/fhalim/http-bridge-extension.svg?branch=master)](https://travis-ci.org/fhalim/http-bridge-extension)

# Prerequisites

- Java 1.5 or later

# Building

`./gradlew package`

# Installation

- Create directory `monitors/HttpBridge` in the AppDynamics Machine Agent directory.
- Copy http-bridge-extensions-1.0.jar from `build/libs` into `monitors/HttpBridge`
- Copy `src/main/resources/config/monitor.xml` into `monitors/HttpBridge`
- Ensure that the values in the `task-arguments` section in `monitor.xml` are satisfactory.

# Posting Data

- Request should be posted to `http://localhost:8888/hostmetrics/metric`

## Fields

- name
- value
- aggregationtype
- rolluptype
- clusterrolluptype