# fix.core

FIXME: description

TODO:
* Add description for `lein gen` -> code generation necessary when no definitions present


;DONE (1) validate the actual key value pairs through the component check
;TODO (2) test more complex message types
;DONE (3) clean up logging with timbre -> extend to other namespaces
;DONE (4) implement core (HERE) -> combine parse and msg/spec validation and think about which methods to open (tests!)
;DONE (4) write parser for ASCII FIX and map correctly to current format used
;DONE all values given for validation at field level are still raw string!!! make parsing with (edn/read-string) part of validation at lowest level
;DONE (5) move duplications to util
;DONE (6) throw out all old namespaces
;TODO (6) remove test.edn
;TODO (7) go through all remaining TODOS
;TODO (8) remove debug (run-tests)
;DONE (9) update lein dependencies and clj version
;TODO (10) write nice README 
     ;TODO which features? 
     ;TODO how to use (require etc) with examples
     ;TODO warning for Regex delimiters
     ;TODO basic insights about FIX nested possibilities
;TODO (11) publish on Clojars


; component :content :attrs -> always "No..." of type NUMINGROUP == first field in group
; component :content :content is array of all repeated fields -> multiple of NUMINGROUP

; message > component > group -> group never directly part of message
; component > group > component -> groups can contain components

; components and fields can have the same name "DerivativeSecurityXML"

; component definitions don't have "required" within top-level attrs -> only the usages have




## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar fix.core-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
