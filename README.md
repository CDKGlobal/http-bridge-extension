# Introduction

Simple extension to allow writing custom metrics to AppDynamics

[![Build Status](https://travis-ci.org/fhalim/http-bridge-extension.svg?branch=master)](https://travis-ci.org/fhalim/http-bridge-extension)

# Prerequisites

- Java 7 or later

# Building

`./gradlew clean check zip`

# Installation

- Unzip `./build/distributions/http-bridge-extension-1.0.zip` into the machineagent `monitors` directory
- Ensure that the values in the `task-arguments` section in `monitor.xml` are satisfactory.

# Posting Data

- Request should be posted to `http://localhost:9999/hostmetrics/metric` as a [form](http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1)

## Fields

- [name](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Java#BuildaMonitoringExtensionUsingJava-MetricPath): Pipe(|) separated path of the custom metric.
- value: long value of the metric
- [aggregationtype](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Scripts#BuildaMonitoringExtensionUsingScripts-AggregationQualifier)
- [rolluptype](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Scripts#BuildaMonitoringExtensionUsingScripts-TimeRollUpQualifier)
- [clusterrolluptype](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Scripts#BuildaMonitoringExtensionUsingScripts-ClusterRollUpQualifier)