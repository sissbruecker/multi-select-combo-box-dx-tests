# Multi Select Combo Box DX Test

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project.

## Tasks

This is a mock application for planning software updates for Tesla vehicles. In this fictional process, users plan when
a new software update is going to be deployed to Tesla cars. For this they have a master-detail view, where they can 
see the existing planned updates in a grid, and have a form for creating and editing updates.

Until now, an update only had a version number and a release date, and was applied to all car models. Due to changes in 
the update process, an update now must only be applied to specific car models (each update applies to one or more models). 
The backend has already been adapted, and the task is now to update the form, so that users can specify which models an 
update should be applied to.

For the **Flow task**, open `src/main/java/com/example/application/views/updates/SoftwareUpdateForm.java`, and follow 
the instructions there.
