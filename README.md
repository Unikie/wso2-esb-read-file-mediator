# WSO2 ESB Read File Mediator
![Build status](https://circleci.com/gh/Mystes/wso2-esb-read-file-mediator.svg?style=shield&circle-token=7223394f667851fc9e237b74e92b87fce5129bc3)

## What is WSO2 ESB?
[WSO2 ESB](http://wso2.com/products/enterprise-service-bus/) is an open source Enterprise Service Bus that enables interoperability among various heterogeneous systems and business applications.

## Features
Read File Mediator is an helper mediator to read XML file content into the current payload either directly to SOAP body element or to defined target element.

## Usage

### 1. Get the WSO2 ESB Read File Mediator jar

You have two options:

a) Add as a Maven/Gradle/Ivy dependency to your project. Get the dependency snippet from [here](https://bintray.com/mystes/maven/wso2-esb-read-file-mediator/view).

b) Download it manually from [here](https://github.com/Mystes/wso2-esb-read-file-mediator/releases).

### 2. Install the mediator to the ESB
Copy the `wso2-esb-read-file-mediator-x.y.jar` to `$WSO2_ESB_HOME/repository/components/dropins/`.

### 2. Use it in your proxies/sequences
Mediator can be used as follows:
```xml
<readFile (fileName="/path/to/file.xml" | property="fileNamePropertyName") [attachXpath="expression"] />

```

#### Example: using by fileName attribute
```xml
<readFile fileName="Example.xml" />

```

#### Example: using by property attribute
```xml
<readFile property="fileNameProperty" />

```

#### Example: using by fileName and attachXPath attribute
```xml
<readFile fileName="Example.xml" attachXpath="//target" />

```

#### Example: using by property and attachXPath attribute
```xml
<readFile property="XML_FILE" attachXpath="//target" />

```

## Technical Requirements

#### Usage

* Oracle Java 6 or above
* WSO2 ESB
    * Wrapper Mediator has been tested with WSO2 ESB versions 4.8.1, 4.9.0 & 5.0.0

#### Development

* Java 6 + Maven 3.0.X

### Contributors

- [Kalle Pahajoki](https://github.com/kallepahajoki)
- [Kreshnik Gunga](https://github.com/kgunga)
- [Ville Harvala](https://github.com/vharvala)

## [License](LICENSE)

Copyright &copy; 2016 [Mystes Oy](http://www.mystes.fi). Licensed under the [Apache 2.0 License](LICENSE).
