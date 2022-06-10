# Internet Movie GRAPH Database (IMRDB)

# Getting Started
These instructions will get you a view of the *`Internet Movie Graph Database`* microservices up and running on your local machine for development and testing purposes.\
In the first step, you should change current path to the project directory

``` 
lunatech 
├── deployment (project for up neo4j and load dataset on Neo4j)
│   ├── README.md
│   ├── docker-compose.yml ( docker-compose up -d run neo4j )
│   ├── extended (directory for extend information in dataset at this project we ignore them)
│   │   ├── load-ext.cypher (download and filter and transform data) 
│   │   └── load-ext.sh (download and filter data) 
│   ├── primary (Movie,Person,Role dataset)
│   │   ├── load-prime.cypher  (Cypher Queries for load main data)
│   │   └── load-prime.sh (download and filter and transform data) 
│   └── queries.cypher (some other usefull command for sysadmin)
└── imgdb  <PROJECT_DIRECTORY>
    ├── README.md
    └── build
        ├── classes
        │   └── java.main.nl.lunatech.movie.imgdb (main package)
        ├── generated.sources.annotationProcessor.java.main.nl.lunatech.movie.imgdb.translator (MapStruct artifact)
        │    ├── MovieMapperImpl.java
        │    └── PersonMapperImpl.java
        └── libs
            └── imgdb-1.0.jar
```
```
~# cd PROJECT_DIRECTORY 
```
Some tips:
annotations used from libraries like `Lombok` and `Mapstruct`, IDEs maybe couldn't detect their boilerplates. don't worry 
the project would compile by no bother from terminal. 

## How To Build
```
~# gradle clean build 
```

## How Run
you can run project directly by `java` command or handle it by container both of them run a same port `5001` 
###Run jar file
```
~# java -jar build/lib/imgdb-1.0.0.jar
```

###Run Docker container

```bash
~# docker-compose  -f up 
```

Open [localhost:5002/swagger-ui/index.html](http://localhost:5002/swagger-ui/index.html)


##Sample
```bash
echo 'Add new Potentials'
curl http://localhost:5001/person?name=Kevin Bacon

```

















