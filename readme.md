# homework

Homework challenge for Griffin interview.

The brief:

>Write a simple program that processes two datafeeds (one in CSV, one in JSON) and produces lists of individuals who are only present in one of the lists. The output should be a single CSV which has the email address, full name of employee and a note identifying which list the user is present in.

## Usage

Open the 'release' folder. In a terminal, run:

    `$ java -jar homework-v1.0-standalone "google.csv" "hr.json"`

`output.csv` contains entries that are unique to either the CSV or the JSON, with a note identifying which source they come from.

## To Do

- Consolidate file readers and make them file-type independent
- Change hard-coded filepaths.
- Change -main to take filepaths in either order.
- Allow user to specify output.csv location.
- Remove leading colons from keywords in output.
- Make code generic and not tailored specifically to :Emails key
