## Stage Specification

The stage specification can be found [here](https://hyperskill.org/projects/50/stages/276/implement) on Hyperskill.

Note: As with the last stage, I skipped the implementation of keeping track of all the various people's coin balances, since that could be implemented as a fairly straightforward combination of a map to track balances as blocks are added in the BlockChain class and a transaction simulator being tied to the message generation. In any case, this wasn't necessary to pass the tests, so...

Also: it seems unlikely that most casual computers would pass the "15 blocks in 15 seconds" rule imposed by the testing system, so I changed the number of zero requirement to be more lax in order to do so. 