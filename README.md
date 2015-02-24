# Introduction

Simple extension to allow writing custom metrics to AppDynamics

[![Build Status](https://travis-ci.org/fhalim/http-bridge-extension.svg?branch=master)](https://travis-ci.org/fhalim/http-bridge-extension)

# Prerequisites

- Java 7 or later

# Building

`./gradlew package`

# Installation

- Create directory `monitors/HttpBridge` in the AppDynamics Machine Agent directory.
- Copy http-bridge-extensions-1.0.jar from `build/libs` into `monitors/HttpBridge`
- Copy `src/main/resources/config/monitor.xml` into `monitors/HttpBridge`
- Ensure that the values in the `task-arguments` section in `monitor.xml` are satisfactory.

# Posting Data

- Request should be posted to `http://localhost:9999/hostmetrics/metric` as a [form](http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1)

## Fields

- [name](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Java#BuildaMonitoringExtensionUsingJava-MetricPath): Pipe(|) separated path of the custom metric.
- value: long value of the metric
- [aggregationtype](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Scripts#BuildaMonitoringExtensionUsingScripts-AggregationQualifier)
- [rolluptype](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Scripts#BuildaMonitoringExtensionUsingScripts-TimeRollUpQualifier)
- [clusterrolluptype](https://docs.appdynamics.com/display/PRO14S/Build+a+Monitoring+Extension+Using+Scripts#BuildaMonitoringExtensionUsingScripts-ClusterRollUpQualifier)