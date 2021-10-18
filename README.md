# vkas
VKAS - a genetic function finder

Version:
========
1.0

Usage:
======
From the folder containing Vkas folder, type following -
java vkas VkasConfig.xml VkasData.xml

You may provide your own config and data files in
xml format.

Mandatory Requirement:
======================
Java Runtime version 1.4 or greater should be available.

How to build the source:
========================
* Create an eclipse java project
* Create a pakcage named vkas in it.
* Add all .java files except vkas.java in the package.
* Keep vkas.java in default package.
* Set compiler compliance level to 1.4 (i.e. java 1.4) or higher
* Build.

License:
========
LGPL (Lesser GPL) version 2.1 (See license)

Copyright:
==========
Shailendra Jain (SLKJain at Yahoo.com) 

Overview of vkas:
=================
vkas is a genetic function or relationship finder software.
It can be used to find out the relationship between a set of
independent and dependent data.

For example, see the following table -
+-------------------------+
+---- a ---  b ---  y ----+
+     1      2      5     +
+     3      1      5     +
+     7      5     17     +
+-------------------------+

If a and b are independent variables and y is a dependent
variable then what is the relationship between a, b and y?

Here it is "y = a+2*b".

This was a very simplistic example.
There are many methods to discover such relationships.
However, there can be times when none of them really work!

In such case genetic algorithm can be very useful to find
an approaximate answer. However, it can not be guaranteed
that the algo will discover the answer within some
predetermined time.

The performance of genetic algo can be varied by adjusting
various genetic parameters passed to it.			

In 'vkas' the data and config params are passed through
two xml files at command-line.

When vkas is run, it creates a population of chromosomes
at random. This population is then genetically modified and
next population is generated. Each population is subjected
to fitness calculation and if a suitable answer chromosome
is found then vkas stops and the answer is printed.
If a suitable answer is not found then the program stops
after specified max generations are over.

a sample run is shown at the end of this file.

Format of configuration file:
=============================
Please see the attached VkasConfig.xml

Format of data file:
====================
Please see the attached VkasData.xml

Summary of a test run:
======================

***************************************************************************
                          vkas (a genetic function finder)
                          --------------------------------
    version              : 1.0
    webpage              : https://sourceforge.net/projects/vikasg/
    copyright            : (C) 2005 by Shailendra Jain
    email                : SLKJain at Yahoo.Com
***************************************************************************
***************************************************************************
*                                                                         *
*   This program is free software; you can redistribute it and/or modify  *
*   it under the terms of the GNU Lesser General Public License as        *
*   published by the Free Software Foundation; either version 2.1 of the  *
*   License, or (at your option) any later version.                       *
*                                                                         *
***************************************************************************

Final Population 0
Fitness = 100.0
+b-+a++aabbbababa--+ababbaaaaaabaa-aaaab--bbaababaa
Checking Population 1
GeneTransposition is not performed.
Checking Population 2
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
Checking Population 3
GeneTransposition is not performed.
Checking Population 4
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
GeneTransposition is not performed.
Checking Population 5
RIS transposition declined in this chromosome (Len==0).
GeneTransposition is not performed.
Checking Population 6
RIS transposition declined in this chromosome (Len==0).
GeneTransposition is not performed.
Checking Population 7
GeneTransposition is not performed.
Checking Population 8
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
GeneTransposition is not performed.
Checking Population 9
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
Checking Population 10
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
Checking Population 11
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
Checking Population 12
One mutation declined, codeIdx=0; newCode=a
Checking Population 13
RIS transposition declined in this chromosome.
GeneTransposition is not performed.
Checking Population 14
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
GeneTransposition is not performed.
Checking Population 15
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
GeneTransposition is not performed.
Checking Population 16
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
Checking Population 17
One mutation declined, codeIdx=0; newCode=a
Checking Population 18
One mutation declined, codeIdx=0; newCode=a
Checking Population 19
RIS transposition declined in this chromosome.
Checking Population 20
GeneTransposition is not performed.
Checking Population 21
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
Checking Population 22
One mutation declined, codeIdx=0; newCode=a
Checking Population 23
One mutation declined, codeIdx=0; newCode=a
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome.
Checking Population 24
Checking Population 25
GeneTransposition is not performed.
Checking Population 26
One mutation declined, codeIdx=0; newCode=a
RIS transposition declined in this chromosome (Len==0).
Checking Population 27
One mutation declined, codeIdx=0; newCode=a
GeneTransposition is not performed.
Checking Population 28
RIS transposition declined in this chromosome (Len==0).
GeneTransposition is not performed.
Checking Population 29
Checking chromosome against the data table...
a = 1.0; b = 1.0; Expected = 2.0; Computed = 2.0
a = 1.0; b = 2.0; Expected = 3.0; Computed = 3.0
a = 3.0; b = 1.0; Expected = 4.0; Computed = 4.0
a = 44.0; b = 4.0; Expected = 48.0; Computed = 48.0
a = 3.0; b = 33.0; Expected = 36.0; Computed = 36.0
a = 23.0; b = 11.0; Expected = 34.0; Computed = 34.0
a = 0.0; b = 12.0; Expected = 12.0; Computed = 12.0
a = 13.0; b = 8.0; Expected = 21.0; Computed = 21.0
a = 111.0; b = 2.0; Expected = 113.0; Computed = 113.0
a = 4.0; b = 101.0; Expected = 105.0; Computed = 105.0
Final Population 29
Fitness = 100.0
aa+-+-aababbbabbb-++a-a++bbbbbbaba+bb--bb+abaabbaba
Resultant expression = {a}+{((a+((b+b)-b))-(a+(b+b)))}+{(b+b)}

=============
<end of file>
