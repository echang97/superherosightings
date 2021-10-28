# superherosightings-echang97
A full stack Java application that reports sightings of (imaginary) superheroes.

This is a cumulative class project for The Software Guild I have worked on for about a week. 

It implements the MVC design pattern utilizing Thymeleaf and Spring Boot.

## What is this project about?
There are superheroes in our midst! Report any sightings you see on this web app.

Be sure you know who, where and when the event happened. If a hero, location, or organization is missing please add them to our database.

More elaborate details on implementation will be in the wiki.

## Assumptions
* Last 10 Sightings should be sorted by date, then id
* Location coords will be repersented by two decimal values: latitude and longitude
* Location and Superpower are independent entities
* Heroes, Sighting, and Organization are dependent on one or mroe entities
* Deletion confirmation will be handled by an extra page or Javascript

## Implemented
* CRUD operations for:
    * Heroes
    * Superpowers
    * Locations
    * Sightings
    * Organizations
* Get Orgs for Hero
* Get Locations for Hero
* Get Heroes in Org
* Get Heroes in Sighting
* Search sighting by Date
* (Partial) Google Maps for Sighting

## Limitations
* Not enough time to implement hero photos
    * Would have to look up more documentation (and possibly more packages)
* Input Validation limited
* Hero details does not specify sighting id
* Empty Organizations and Sightings should be deleted
* Website could look nicer
