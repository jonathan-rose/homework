# Homework Challenge

Homework challenge for Griffin interview.

The brief:

>Write a simple program that processes two datafeeds (one in CSV, one in JSON) and produces lists of individuals who are only present in one of the lists. The output should be a single CSV which has the email address, full name of employee and a note identifying which list the user is present in.

## Usage

Open the 'release' folder. In a terminal, run:

    $ java -jar homework-v1.0-standalone "google.csv" "hr.json"

`output.csv` contains entries that are unique to either the CSV or the JSON, with a note identifying which source they come from.

## To Do / Potential Improvements

- Consolidate file readers and make them file-type independent
- Change hard-coded filepaths.
- Change -main to take filepaths in either order.
- Allow user to specify output.csv location.
- Remove leading colons from keywords in output.
- Make code generic and not tailored specifically to :Emails key
- Error handling


## Written Question

*Imagine that you are building a system that is responsible for performing many different audits at automated periodic intervals (such as hourly, daily and monthly), these audits will often require fetching multiple different data feeds and then processing, producing a report and forwarding onwards. Describe how you would approach building a piece of software to do this? What technologies would you use?*

*Please explain your answers and justify your choices, feel free to make assumptions and state them, and less than 500 words in total please.*

### Approach:

I would start by speaking to the people wanting the reports. I want to know exactly what information they want and in what format. How do they want to access the reports, and do they plan on doing additional processing on the information? For example, do they want to receive a static document such as a PDF, or do they want the report to be machine-readable, structured data for further processing, such as JSON or XML? 

Another example question: How do they want to access the reports? Will it be from any device, in which case an online, user-accessible front-end will be required, or do they want to receive automatic reports without interaction with the system, in which case a UI will not be necessary?  This would determine the behaviour of the system and a set of requirements, dictating the essential functionality that it needs.

I am assuming that the data will relate to financial transactions, so we also need to discuss how both the data used and the report itself will be secured. This will determine what sort of permissions system will be in place. Discussions involving security and compliance should be a priority, as this may place constraints on the design of the system that are not negotiable. For example, it may be the case that the reports absolutely cannot be sent to a user via email, to protect against the emails being forwarded on to untrusted third parties.

### Technology:

I am going to assume that I am building a web-based front-end accessible from any device, that generates reports about accumulated data.

The UI should be responsive to change, potentially even showing updates in real-time. A framework like SvelteKit could be used to do fast, server-side processing of data that without exposing the source data client-side.

A fast, scalable database will be needed, capable of receiving hundreds, thousands, or even millions of read / write operations every second, without inconsistences being introduced by multiple client access. The design of the system should be scalable, so that as user demand increases, so too can the capacity of the database. Using PostgresSQL, or a derivative of it, would offer concurrency control that prevents inconsistencies between read / write operations form different clients.

I would use a cloud-hosting provider like AWS or Google Cloud to host the system, however I would containerize the software using a system like Docker to decrease the likelihood of platform lock-in overtime.
