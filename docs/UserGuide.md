---
layout: page
title: User Guide
---

RosterBolt is a **desktop app for managing team contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, RosterBolt can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-T12-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your RosterBolt.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to RosterBolt.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to RosterBolt.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​ [r/ROLE] [nt/NOTES] [va/AVAILABILITIES]…​ [vr/RECORDS]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags, availabilities, and records (including 0). Role and notes are optional.
</div>

* A person is considered a duplicate if the phone number matches exactly, or the email matches case-insensitively.
* `AVAILABILITIES` must be in the format `DAY,HH:mm,HH:mm` (day, start time, end time) where `DAY` is a full uppercase day name (e.g., `MONDAY`) and start time is earlier than end time.
* `RECORDS` must be in the format `yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm` (start date-time, end date-time) and start date-time must be earlier than end date-time.

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 r/Usher nt/Weekend only va/MONDAY,14:00,17:00 vr/2026-03-20T14:00,2026-03-20T17:00`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal r/Logistics nt/Prefers morning shifts va/SATURDAY,09:00,12:00 va/SUNDAY,13:00,16:00`
* `add n/Alex Tan p/91234567 e/alex@example.com a/NUS`

### Listing all persons : `list`

Shows a list of all persons in RosterBolt, optionally sorted by an attribute.

Format: `list [ATTRIBUTE [asc|desc]]`

* Currently supported `ATTRIBUTE`: `name`, `phone`, `email`, `role`, or `tag`.
* Order defaults to `asc` when omitted.
* Omitting `ATTRIBUTE` shows the list in the default order.

Examples:
* `list`
* `list name`
* `list email desc`

### Showing recycle bin of recently deleted persons : `bin`

Shows the recycle bin, which contains all recently deleted persons in RosterBolt.

Format: `bin`

* Persons deleted by the `clear` and `delete` commands will be added to the recycle bin.
* The recycle bin is cleared when the application is closed.

### Editing a person : `edit`

Edits an existing person in RosterBolt.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [r/ROLE] [nt/NOTES] [t/TAG]…​ [va/AVAILABILITIES]…​ [vr/RECORDS]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, availabilities, or records, existing values of that field will be replaced (i.e. adding is not cumulative).
* You can remove all the person’s tags, availabilities, or records by typing `t/`, `va/`, or `vr/` without specifying values after the prefix.
* `AVAILABILITY` format: `DAY,HH:mm,HH:mm` (e.g., `MONDAY,14:00,17:00`).
* `RECORD` format: `yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm` (e.g., `2026-03-20T14:00,2026-03-20T17:00`).

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com va/MONDAY,18:00,20:00` Edits the phone number, email address, and availability of the 1st person.
*  `edit 2 n/Betsy Crower t/ va/ vr/` Edits the name of the 2nd person and clears all existing tags, availabilities, and records.

### Locating persons by keyword: `find`

Finds persons whose fields contain any of the given keywords.

Format: `find [m/MATCH_TYPE] KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* The search covers all fields: name, phone, email, address, role, notes, and tags.
* `m/kw` matches full words only. e.g. `Han` will not match `Hans`
* `m/ss` matches substrings. e.g. `Han` will match `Hans`
* `m/fz` allows small spelling mistakes. Words that are up to 2 simple edits away (in terms of adding, removing, or changing a letter) can still match. e.g. `michigan` will match `michegan`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`
* `MATCH_TYPE` is optional. When omitted, keyword matching is used.
* Currently supported `MATCH_TYPE`: `kw`, `ss`, `fz`.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)
* `find m/kw John` also returns `john` and `John Doe`
* `find m/ss ali` returns `Alice Pauline` and `Ali Tan`
* `find m/fz michigan` returns `Elle Meyer` (address: `michegan ave`)

### Deleting a person : `delete`

Deletes the specified persons from RosterBolt.

Format: `delete INDEX [MORE_INDICES]`

* Deletes the person at the specified indices.
* Indices refer to index numbers shown in the displayed person list.
* Indices **must be positive integers** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2 3` deletes the 2nd and 3rd persons in RosterBolt.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from RosterBolt.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

RosterBolt data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

RosterBolt data are saved automatically as a JSON file `[JAR file location]/data/rosterbolt.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, RosterBolt will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the RosterBolt to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous RosterBolt home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​ [r/ROLE] [nt/NOTES] [va/AVAILABILITIES]…​ [vr/RECORDS]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague r/Usher nt/Available weekends va/FRIDAY,19:00,21:00 vr/2026-03-20T14:00,2026-03-20T17:00`
**Bin** | `bin`
**Clear** | `clear`
**Delete** | `delete INDEX [MORE_INDICES]`<br> e.g., `delete 2 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [nt/NOTES] [t/TAG]…​ [va/AVAILABILITY]…​ [vr/RECORD]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com va/MONDAY,14:00,17:00`
**Find** | `find [m/MATCH_TYPE] KEYWORD [MORE_KEYWORDS]`<br> e.g., `find m/kw James Jake`, `find m/ss ali`, `find m/fz michigan`
**List** | `list [ATTRIBUTE [asc|desc]]`<br> e.g., `list name desc`
**Help** | `help`
