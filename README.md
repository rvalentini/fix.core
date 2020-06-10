# fix.core
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.rival/fix.core.svg)](https://clojars.org/org.clojars.rival/fix.core) <br/>
Financial Information eXchange (FIX) protocol validator using clojure.spec <br/>

## Installation

Add the following dependency to your project:
```
[org.clojars.rival/fix.core "1.0.0"]
```

## Usage
Just refer the `valid?` method from `fix.core` namespace. The method takes two parameters:
1) Fix message as string
2) Custom delimiter (optional) 
```
(ns fix.usage
  (:require [fix.core :refer [valid?]]))

;Valid Heartbeat FIX message with standard SOH-delimiter
(def fix-message "8=FIXT.1.19=6535=049=BuySide56=SellSide34=352=20190605-12:45:24.9191128=910=064")
(valid? fix-message)
=> true

;Valid Heartbeat FIX message with custom '%' delimiter
(def fix-message "8=FIXT.1.1%9=65%35=0%49=BuySide%56=SellSide%34=3%52=20190605-12:45:24.919%1128=9%10=064")
(valid? fix-message #"%")
=> true

;Invalid Heartbeat FIX message where the checksum (tag 10) is missing in the end
(def invalid-fix-message "8=FIXT.1.19=6535=049=BuySide56=SellSide34=352=20190605-12:45:24.9191128=9")
(valid? invalid-fix-message)
=> false

```

## What is FIX?

The Financial Information eXchange protocol is an open source communications protocol for the real-time exchange of 
financial information. It is broadly used by financial institutions and other financial market actors to trade securities. 
FIX is the standard electronic protocol for financial trade executions worldwide. 

The message format defined by the protocol is called "tagvalue", which is basically a sequence of ASCII encoded key-value
pairs delimited by a special SOH control character. The binary wire format for FIX messages is called "Simple Binary Encoding", 
which is exchanged via TCP-socket connections between parties. 
  
The FIX protocol standard is defined and maintained by the "FIX Trading Community Organization" (see 
https://www.fixtrading.org/online-specification/introduction/ for more details about the FIX protocol and
https://www.fixtrading.org/standards/fix-5-0-sp-2/ for the actual FIX protocol definitions)

## Why a FIX message validator?

The initial motivation for this library was my interest in clojure.spec. I was trying to get my head around 
clojure.spec for some time and thought the most effective way to do so, was to apply it to an existing, real-world 
protocol. Retrospectively, this was not the best idea, since I spent significantly more time implementing the complex 
FIX validation logic, than actually exploring the depths of clojure.spec. I have no connection to the finance industry 
whatsoever, so the reason why I picked FIX as test subject is pretty arbitrary and rooted in my personal interest in the
mechanics of financial markets. This library was created from a purely technical perspective on the FIX-protocol, I have
no deeper understanding of how professionals are actually using this protocol on a daily basis, which probably had
an impact on my implementation approach. 

This validation library is intended as a debugging tool for developers who are implementing FIX-based
trading applications and who need an easy way to validate the correctness of FIX messages. I initially planned to 
embed this library into a slim web application to offer the validation functionality as REST service, which I still
might do, in case there is demand for it. 

I am always grateful for any feedback, technical, as well as non-technical feedback (maybe on my understanding of FIX) 
is greatly appreciated :)      

## Features

Validation features:
* Complete StandardHeader validation including "fixed ordering" tags like BeginString, BodyLength, MessageType
* Complete StandardTrailer validation including checksum calculation
* Data type of every single tag included in the message is checked: e.g. UTCDateOnly, TZTimeOnly, Country, MultipleCharValue ... 
* Allowed enums for tag values are considered: e.g. some allowed values for the field ExecInst (18): <br/>
```
7 = Strict scale <br/>
B = OK to cross <br/>
G = All or none - AON <br/>
j = Single execution requested for block trade
```
* Distinguishes between required/non-required fields according to the component definition
* Deep message validation: according to the message type definition specified through the tag MessageType (:35), 
the message can contain nested combinations of components and groups. The validation is executed according to this 
nested structure. Fields that are contained in an embedded component or group are validated in their respective context,
meaning that possible field repetitions are evaluated with repect to their surrounding structures. 
* Group validation considers the preceding num-in-group field, which defines the numbers of repetitions of the proceeding group. 
A divergence between the num-in-group count and the actual number of repetitions will fail the validation.
* The ordering of tags is considered arbitrary for fields of the same component (this might not be 100% accurate in all possible cases!),
but fixed for the fields of the same group.
* In case of invalid messages, a (hopefully helpful) log statement indicates the reason why the validation failed.


***Note***: The individual definitions for components, groups and fields are programmatically extracted from XML source files,
which can be found at https://github.com/quickfix/quickfix/tree/master/spec

***Note***: The FIX message validation functionality provided in this library does not 
cover 100% of all restrictions defined in the original FIX protocol! Some field specific restrictions, which are
mentioned within the comments/description section cannot be extracted programmatically and are therefore not included
in the validation: 
E.g. for field ExecInst (18), some of the allowed Enum values are mutually exclusive. The validation would not 
detect any violation of this mutual exclusivity. 
```
l = Suspend on system failure (mutually exclusive with H and Q)
m = Suspend on Trading Halt (mutually exclusive with J and K)
n = Reinstate on connection loss (mutually exclusive with o and p)
o = Cancel on connection loss (mutually exclusive with n and p)
```

## Generating FIX Definitions

The FIX protocol definitions for **fields, components** and **groups** are included in **/src/fix/definitions** as Clojure maps.
The definition files are generated from the XML protocol definition files, which can be found under **/resources**.  
The following two files combined defined all relevant data types for the **FIX 5.0 SP2** :
```
/resources/FIX50SP2.xml
/resources/FIXT11.xml
```
The `_combined.xml` version contains both definition files. <br/>
In case you want to re-generate the FIX protocol definitions (maybe in case of a FIX version update), you can just run
```
lein generate-definitions
```


***Note***: In case you want to use a custom delimiter other than the default SOH-delimiter, be careful which character
you choose! The delimiter must not be part of the regular FIX message payload and it is also not recommended to use 
regex control characters as delimiter, which can lead to unexpected side effects and produce incorrect validation results! 

***Note***: Any custom delimiter must be given as a single character regex pattern (java.util.regex.Pattern) of the form #"[char]"


## License

Copyright © 2020 Riccardo Valentini

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
