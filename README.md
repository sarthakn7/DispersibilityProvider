# Dispersibility Provider

This is a web application that can be used to disperse a service using Gigapaxos. This web app inputs the service details and a jar that contains an implementation of [Replicable](https://github.com/MobilityFirst/gigapaxos/blob/master/src/edu/umass/cs/gigapaxos/interfaces/Replicable.java). These details are stored in Cassandra, where they can be accessed by Gigapaxos running Dispersibility wrapper. This also creates the service on Gigapaxos.

## How to run

mvn clean spring-boot:run

Please see the [application.yaml](https://github.com/sarthakn7/DispersibilityProvider/blob/master/src/main/resources/application.yaml) file for the parameters that can be used to modify the cassandra connection as well as to run the create-service jar that is used to create the service on Gigapaxos.
The schema for Cassandra can be found in [app.cql](https://github.com/sarthakn7/DispersibilityProvider/blob/master/src/main/resources/app.cql).
