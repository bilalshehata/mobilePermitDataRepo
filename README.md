# Mobile Food Permit Storage


# Requirements
Create a backend service that provides access to all mobile food permits.

## functionalities
The API should support the following frontend use cases.
- Geo queries by GPS location and radius
- Autocomplete by name and/or other relevant dimensions
## The API
Once deployed, full API specification is available at
http://localhost:8080/swagger-ui.html
the endpoint will use JSON objects to communicate data between backend and frontend servers.
A high level overview is provided below: 

main endpoints

To return all permits

    GET /getall
    response: All permits in memory

To return all permits by page, this method is better for scalability 

    GET /getallpaginated
    params: pagenumber , pagesize
    response: returns the page number based on the page size

To return all permits within a particular radius of a given point
    
    GET /getbyradius
    params: coordinates latitude , longitude, radius (meters)
    response: All permits within the radius from the coordinate provided.

To return all permits with applications starting with the name provided, frontend to use this for autocomplete. 


    GET /getbynamestartswith
    params: name (or part of)
    response: returns all application names starting with the name provided

Add a new permit to memory

    POST /create
    body : { locationId (mandatory),
      applicant,
      facilityType,
      status, 
      latitude, 
      longitude} 
    respose: the permit that was stored

   




## The Data
the data used for this requirement can be found at https://data.sfgov.org/Economy-and-Community/Mobile-Food-Permit-Map/px6q-wjh5
Mobile Food Facility Permits including name of vendor, location, type of food sold and status of permit.
The data in this instance is inherently spatial with each permit being placed at a specific X,Y coordinate.


# Implementation

## Design
Based on the requirements the design should be optimised to handle spatial range queries.
This implementation will store the data in memory rather than creating a new DB. The code is flexible such that a database could be added at a later stage.


### Storage Structure
Although a hashmap will allow us to store and get these points at O(1) time complexity it won't be able to perform range queries as there is no inherent ordering. 
To store the data we have opted to implement this using a Kd-Tree instead. The benefit of adopting this datastructure is that it is optimised to handle spatial range queries as instead of needing to compare each node to each other node O(n^2) this datastructure allows for this sort of comparison to occur at O(logn).
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Kdtree_2d.svg/740px-Kdtree_2d.svg.png"/>

### Coordinate System Considerations
The data in the dataset is stored in latitude and longitude values which provides coordinates in degrees.
However, this does not lend itself to distance / area calculations as the radius of the earth needs to be taken into consideration.
For this reason we have projected the long lat values onto a 2D plane using WGS 84 / Pseudo-Mercator https://epsg.io/3857.
This projected value will act as our key for the Kd-Tree therefore providing the ability to perform range queries more easily.

# Running
In the directory please run in the following order to bring up a docker container running the program.
 
    gradle clean build 
    docker build .
    docker-compose up
this will run on port 8080 by default
# Testing
## With Curl
Check status
```
curl localhost:8080/actuator/health
```
should receive a JSON {STATUS:UP} message. 
## Configurations
in application.properties

    csv_location (csv that will initilise the memory)

    max_autofill (number of results to return for an autofill query)