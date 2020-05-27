# fix.core

Financial Information eXchange (FIX) protocol validator using clojure.spec <br/>

TODO where can the definitions be found? FIX homepage
TODO why section?


TODO include clojar tag
TODO (10) write nice README 
     ;TODO which features? 
     ;TODO how to use (require etc) with examples
     ;TODO warning for Regex delimiters
     ;TODO basic insights about FIX nested possibilities
TODO (11) publish on Clojars


; component :content :attrs -> always "No..." of type NUMINGROUP == first field in group
; component :content :content is array of all repeated fields -> multiple of NUMINGROUP

; message > component > group -> group never directly part of message
; component > group > component -> groups can contain components

; components and fields can have the same name "DerivativeSecurityXML"

; component definitions don't have "required" within top-level attrs -> only the usages have


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

TODO forgot some feature?

Note: The individual definitions for components, groups and fields are programmatically extracted from XML source files,
which can be found at https://github.com/quickfix/quickfix/tree/master/spec

Note: The FIX message validation functionality provided in this library does not 
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
lein gen
```


## Installation

Download from http://example.com/FIXME.

## Usage
How to include lib?
Calling example

FIXME: explanation

    $ java -jar fix.core-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...



## License

Copyright © 2020 TODO

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
