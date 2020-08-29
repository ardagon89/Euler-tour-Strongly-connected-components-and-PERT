README:
======
Long Project # 02: Euler tour, Strongly connected components and PERT

Authors :
------
1) Shariq Ali SXA190016
2) Abhigyan axs190140
3) Enakshi epm180002
4) Navanil nxs190026

How to compile and run the code:
-------
1) The command prompt path should be in "sxa190016" directory
2) Graph.java should be accessible in sibling "idsa" directory
3) javac Euler.java
4) java Euler
3) javac DFS.java
4) java DFS
3) javac PERT.java
4) java PERT

The class Euler find the eulerian circuit if present otherwise returns null will the reason. 
DFS class find the number of strongly connected components.
PERT class models the functionalities of ec, lc, slack etc. methods. 

We have used the main function to run and test the class functions. 

The procedure to run is as follows:

When you run the main function, it will
1. Scanner to read input
2. Read from keyboard or Read from file or Read from text string
3. Fix the start vertex
4. output can be suppressed by passing 0 as third argument
5. Print the graph
6. Get the start vertex
7. Start the timer
8. Create an object of Euler class
9. Create a variable to store the tour vertices
10. End the timer
11. print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)