# Assignment3
Distributed System Assignment3

## How to Run

1. Use command 'make' to compile all java files
2. start the learner with command ***make launch-learner***
3. start the acceptor m4-m9, with ***make launch-acceptor member=x***, x is a number between 4 and 9, use 6 different terminals to do that
For example:  make launch-acceptor member=5
Note: You must start all the acceptors and the learner before proposers.
4. start the proposer m1-m3, with ***make launch-proposer member=x***, x is a number between 1 and 3, they will start proposal after you 
launch them. You can launch at most 3 proposer in different terminals.
For example:  make launch-proposer member=1

Description:
Each member own a terminal, so if you want to run 3 proposers(member 1-3) and 6 acceptors(member 4-9), you need to open 10 terminals(one for
learner). Use random number to simulate the response late time. After launch proposer, it will propose start from number 1. The email message
will print on terminals of each proposers and acceptors. Once the accept state of a proposer reaches 4(majority of 6), the proposer will 
report to learner, and learner will announce the outcome and stop all process.

Note: When a proposer is disconnected, it will report error in terminals of acceptors(also contrary), but it doesn't influence the paxos 
processes, every processes will continue, until the learner announce a member is elected.
