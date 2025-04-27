# Fetch Rewards Take Home Test - Receipt Processor

## Setup Workspace 

```shell
git clone https://github.com/mkannekanti/fetch-receiptsprocessor.git
cd fetch-receiptsprocessor
```

## Package Structure

```shell
.
├── README.md     # README
├── compose.yaml  # Docker compose file
├── Dockerfile    # Dockerfile
├── mvnw*         # Maven build scripts
├── pom.xml       # Spring POM file
├── src/test/java/org/fetch/ # Unit Test files
├── src/main/java/org/fetch/ # Source Code 
    └── resources
    │   ├── application.properties
    │   └── openapi.yaml              # OpenAPI spec for /receipts/process and /receipts/{id}/points
    └── receiptsprocessor
        ├── controller
        │   └── ReceiptsApiDelegateImpl.java  # Implementation of REST API interfaces
        └── service
        │   └── ReceiptsProcessorService.java # Main service handling the requests
        ├── ReceiptsProcessorApplication.java # Webserver runner
        ├── rules                 # reward point rules implementation
        │ ├── common
        │ │ ├── Rule.java         # Rule Interface
        │ │ └── RuleType.java     # RuleType Enum
        │ ├── AmILLMRule.java     # ... Rule implementations ..
        │ ├── ItemPairCountRule.java
        │ ├── OddPurchaseDayRule.java
        │ ├── RetailerNameLengthRule.java
        │ ├── TimeRangeRule.java
        │ ├── TotalMultipleOfQuarter.java
        │ └── TotalRoundDollarAmountRule.java
        │ ├── ItemDescriptionLenMultiplierOf3Rule.java
```

## Build & Run the Service

### Build the service
```shell
./mvnm clean install
```

### Start the docker container
```shell
docker-compose up --build
```

**NOTE:** this will start the web server in the docker container and maps to system's port 8080.

If port 8080 on the system is not available, please change the port mappings in `compose.yaml`


### Accessing the service via Swagger

```markdown
http://localhost:8080/swagger-ui/index.html#/
```

### APIs supported

```shell
http://localhost:8080/receipts/process
http://localhost:8080/receipts/{id}/process
```

## Test the endpoints

### via Commandline

#### POST /receipts/process api

```shell
curl -X 'POST' \
  'http://localhost:8080/receipts/process' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}'
```

#### GET /receipts/{id}/points api
```shell
curl -X 'GET' \
  'http://localhost:8080/receipts/fd194773-6383-4b20-8863-8fbfd1a1ffd4/points' \
  -H 'accept: application/json'
```

### via Swagger

1. Go to swagger for the service and test the API.
2. Click on the API to expand
3. Click on `Try it out` button
4. for POST api, Make changes to the Request body as needed.
5. for GET api, fill in the {id}
6. Click on `Execute`

```shell
http://localhost:8080/swagger-ui/index.html#/
```

