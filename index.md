## Welcome to VKAS

VKAS is a Java based solver that can be used to find out the relationship between a set of independent and dependent variables. I developed it way back in 2005 based on the paper "Gene Expression Programming in Problem Solving" (https://www.gene-expression-programming.com/webpapers/ferreira-WSC6.pdf). VKAS source code was earlier published on SourceForge (https://sourceforge.net/projects/vikasg/). I recently moved the source code to GitHub.

### What method does VKAS use?

VKAS uses GEP (Gene Expression Programming) to discover the relationship between a set of variables. For example, see the following table -
+-------------------------+
+---- a ---  b ---  y ----+
+     1      2      5     +
+     3      1      5     +
+     7      5     17     +
+-------------------------+

If a and b are independent variables and y is a dependent variable then what is the relationship between a, b and y?

Here it is "y = a+2*b".

This, off course, was a very simple example. There are many regression methods to discover such relationships. GEP is one of them.

### Gene Expression Programming

Gene Expression Programming (GEP) is part of the Genetic Algorithm family. It can be used to solve optimization problems, however, it can not be guaranteed that the algorithm will coverge within some predetermined time. The performance of the algorithm can be improved by adjusting the hyper-parameters.	I had read following paper about GEP in 2004-2005 - https://www.gene-expression-programming.com/webpapers/ferreira-WSC6.pdf

### How to use VKAS

In 'vkas' the data and config params are passed through two xml files at command-line.

When vkas is run, it creates a population of chromosomes at random. This population is then genetically modified and next population is generated. Each population is subjected
to fitness calculation and if a suitable answer chromosome is found then vkas stops and the answer is printed. If a suitable answer is not found then the program stops after specified max generations are over.
