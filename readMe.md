                                          ===========OVERVIEW===========

The RestAPI is used to access service resources(insurance information like client details, claim details and insurance policy details).<br>
Security Configuration - All CRUD access to resources are secured with JWT.<br>
Database - JpaRepositories for making the crud operations to the H2 database.<br>
RestController - The controller methods handles appropriate requests and returns the result.<br>
Validations - Validations are also done in controller methods. if any issues, they are returned in response.<br>
Exceptions - Custom Exceptions are available, will be thrown when a call to crud isn't available or a wrong call made.<br>
Unit Test - Unit test done for crud operations and exceptions.
                                    
                                          ===========JWT===========

1.Generate a jwt token,<br> Send a post request to /authenticate - Add Authorizations(username and password) - it will return a token response.


There are two users configured in jwtSecurityConfiguration.<br>

     User 1=> username : admin
              password : password
     User 2=> username : user
              password : password
example:

     {
         "token":"eyJraWQiOiJmMjAzMjA0Zi0wYzQwLTQ2YTktYjY0MS1hMmRhNmI1YzU5MGUiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYWRtaW4iLCJleHAiOjE2ODAyODQ1MjIsImlhdCI6MTY4MDI4MjcyMiwic2NvcGUiOiJST0xFX0FETUlOIFJPTEVfVVNFUiJ9.ihJWyIGXJJM25AN7HlxNiqo4s3lW5HvCg_TIrsyiCL9uBZfRbNAzN3st3e5dQURARXJd0Jgy8HXdnzB-FkmwPQFlw4vUEOFa9j2_dF-vdpDtoQ19uw9zMr6N2lYDY4gnI_L4EB0j8gcG3tyyqDhirxQRPPX_aE59rbPZ8XwwsoUlrFOF50I2Hmv5kDBo-BN-HUhKTKJDWu5Le0MNI6e2uqGLSCefy1ITLYrWCtb1Xt61T0H5wvoOzK5bPndg7a8ETS2Pwu-4Blw_IXH7n2UykExzYv2wcJ1LTcNIdYeoga5OMxMBCQaVZ7Zh26rLMLAS18T43u0_tpgeax7hrCtSjw"
     }

2.Now use the jwt token in every rest api calls,<br>

   Add Headers:
      name = Authorization,
      value = Bearer ${JwtToken}

                                          ===========REQUESTS===========

Clients:

1. Fetch All Client Info => GET<br>
   http://localhost:8080/api/clients

2. Fetch Specific Client Info => GET<br>
   http://localhost:8080/api/clients/{id}

3. Create New Client Info => POST<br>
   http://localhost:8080/api/clients

   sample request body:

       {
            "clientId": 1,
            "name": "Random Name",
            "dateOfBirth": "1999-09-12",
            "address": "4,AAAA-BBBB-CCCC,XXXX,YYYY,ZZZZ",
            "contactInfo": "1234567890"
       }
4. Update Specific Client Info => PUT<br>
   http://localhost:8080/api/clients/{id}

   sample request body:

       {
            "clientId": 1,
            "name": "Random Name Changed",
            "dateOfBirth": "1999-09-12",
            "address": "4,AAAA-BBBB-DDDD,WWWW,YYYY,ZZZZ",
            "contactInfo": "1234567890"
       }

5. Delete Specific Client Info => DELETE<br>
   http://localhost:8080/api/clients/{id}



Insurance Policy:

1. Fetch All Insurance Policy Info => GET<br>
   http://localhost:8080/api/policies

2. Fetch Specific Insurance Policy Info => GET<br>
   http://localhost:8080/api/policies/{id}

3. Create New Insurance Policy Info => POST<br>
   http://localhost:8080/api/policies

   sample request body:

       {
            "policyNumber": 1,
            "clientId": 1,
            "policyType": "Health Insurance",
            "coverageAmount": 200000,
            "premium": 1000,
            "startDate": "1999-09-12",
            "endDate": "2029-09-12"
       }

4. Update Specific Insurance Policy Info => PUT<br>
   http://localhost:8080/api/policies/{id}

   sample request body:

       {
            "policyNumber": 1,
            "clientId": 1,
            "policyType": "Life Insurance",
            "coverageAmount": 230000,
            "premium": 1000,
            "startDate": "1999-09-12",
            "endDate": "2039-09-12"
       }

5. Delete Specific Insurance Policy Info => DELETE<br>
   http://localhost:8080/api/policies/{id}

Note: A Single person can have multiple Insurance Policy.



Claims:

1. Fetch All Claim Info => GET<br>
   http://localhost:8080/api/claims

2. Fetch Specific Claim Info => GET<br>
   http://localhost:8080/api/claims/{id}

3. Create New Claim Info => POST<br>
   http://localhost:8080/api/claims

   sample request body:

       {
            "claimNumber": 1,
            "policyNumber": 1,
            "clientId": 1,
            "description": "insurance claim description",
            "claimDate": "2909-09-12",
            "claimStatus": "REJECTED"
       }

4. Update Specific Claim Info => PUT<br>
   http://localhost:8080/api/claims/{id}

   sample request body:

       {
            "claimNumber": 1,
            "policyNumber": 1,
            "clientId": 1,
            "description": "health claim description",
            "claimDate": "2999-12-30",
            "claimStatus": "APPROVED"
       }

5. Delete Specific Claim Info => DELETE<br>
   http://localhost:8080/api/claims/{id}


                                          ===========VALIDATIONS===========

Validations are done in POST/PUT Methods in the CONTROLLER and the issues are sent back in Response.<br>

Clients:

    @Id
    @NotNull
    private Integer clientId;                                          //Client ID => Unique Data Field - CANNOT BE NULL

    @Size(min = 10, message = "Name length must be 10")
    private String name;                                            //Name-Length Minimum = 10

    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;                                  //Date must be from PAST and Date Pattern Should be yyyy-MM-dd

    @Size(min = 20, message = "Address length must be 20")
    private String address;                                         //Address-Length Minimum = 20

    @Size(min = 10, max = 10, message = "Mobile Number length must be 10")
    private String contactInfo;                                     //ContactInfo/Phone Number-Length = 10 (Not less or high than that)



Insurance Policy:

    @Id
    @NotNull
    private Integer policyNumber;                                      //Policy Number => Unique Data Field - CANNOT BE NULL

    @NotNull
    private Integer clientId;                                          //Client ID associated with a Policy - CANNOT BE NULL

    @Size(min = 5, message = "PolicyType length must be 5")
    private String policyType;                                      //Policy Type-Length Minimum = 5

    @NotNull
    private Long coverageAmount;                                    //Coverage Amount - CANNOT BE NULL

    @NotNull
    private Long premium;                                           //Premium Amount - CANNOT BE NULL

    @PastOrPresent
    private LocalDate startDate;                                    //Policy Start Date must be from PAST or PRESENT

    @Future
    private LocalDate endDate;                                      //Policy End Date should be from FUTURE



Claims:

    @Id
    @NotNull
    private Integer claimNumber;                                       //Claim Number => Unique Data Field - CANNOT BE NULL

    @NotNull
    private Integer policyNumber;                                      //Policy Number associated with a Claim - CANNOT BE NULL

    @NotNull
    private Integer clientId;                                          //Client ID associated with a Claim - CANNOT BE NULL

    @Size(min = 15, message = "Description length must be 15")
    private String description;                                     //Description-Length Minimum = 15

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate claimDate;                                    //Date must be from Future and Date Pattern Should be yyyy-MM-dd

    private ClaimStatus claimStatus;                                //Enum Constants representing the status of the claim(APPROVED, REJECTED, UNDER_PROCESS, DETAILS_REQUIRED).
<br>

      
                                          ===========EXCEPTIONS===========

Custom Exceptions for all three entities based on service methods and send response status of 404,<br><br>
Exceptions: src/main/java/com/restapi/insurancerestapi/exceptions

1.Fetching all entries<br>
=> No Exceptions - findAll() => returns a list of all entries or empty list.

2.Fetching specific entry (based on clientId,policyNumber,claimNumber)<br>
=> Exception - findById() => throwing Custom Exception,if entry doesn't exist.<br>
=> No Exceptions - findById() => returns a specific entry,if entry exists.

3.Creating a entry<br>
=> Exception => throwing Custom Exception, if an entry is already present,we cannot create a new entry with same clientId or policyNumber or claimNumber.<br>
=> No Exceptions => creating a successful entry.

4.Updating a entry<br>
=>Exception =>throwing Custom Exception, we cannot update an entry,if an entry with same clientId or policyNumber or claimNumber doesn't exist.<br>
=> No Exceptions => if an entry is updated successfully.

5.Deleting a entry<br>
=>Exception =>throwing Custom Exception, if an entry doesn't exists.<br>
=>No Exceptions => deleting a entry successfully.

                                          ===========ACTUATOR===========

Use http://localhost:8080/actuator to monitor and manage the spring application.

Exposing all endpoints using below property,<br>
management.endpoints.web.exposure.include=*

                                          ===========TESTS===========

Tests for Services are available in,<br> src/test/java/com/restapi/insurancerestapi/services

<br>

                                          ===========DEPENDENCIES===========
Data Jpa:
spring-boot-starter-data-jpa

RestAPI: 
spring-boot-starter-web

DevTools: 
spring-boot-devtools

H2 Database:
h2

Lombok:
lombok

Validation:
spring-boot-starter-validation

Jwt:
spring-boot-starter-oauth2-resource-server

JUnit:
junit
junit-jupiter-api

Actuator:
spring-boot-starter-actuator
