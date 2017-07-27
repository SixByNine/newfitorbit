Documentation is embedded into the source.

To get started, see
-jaolho.data.lma.implementations.ExampleFit.java
-jaolho.data.lma.LMA.java
-jaolho.data.lma.LMAFunction.java

To make your own fit, implement your own fit function (and it's partial derivates), and create an instance of LMA as is done in ExampleFit.java. Then call LMA.fit(). Be sure to use valid start values for the parameters (i.e., make a good guess before starting the fit).

To test the classes on your machine, go to the unzipped directory and

1) compile with your favourite IDE or from the command line: 
javac -d . -cp jama.jar src/jaolho/data/lma/*.java src/jaolho/data/lma/implementations/*.java

2) run the tests:
java -cp jama.jar:. jaolho.data.lma.implementations.ExampleFit
java -cp jama.jar:. jaolho.data.lma.implementations.MultiDimExampleFit

NOTES:
The package jama.jar contains a matrix class which is needed for the JAMAMatrix implementation.

Have fun!

NOTE 23.03.2007:
Multidimensional fit function LMAMultiDimFunction and fit engine LMAMultiDim added.
They are used almost identically to the one dimensional counterparts.

NOTE 24.4.2007:
LMAMultiDim and LMA combined to one class, LMA, which can now handle both LMAFunctions and LMAMultiDimFunctions.