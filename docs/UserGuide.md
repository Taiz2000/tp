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

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar RosterBolt.jar` command to run the application.<br>
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

* Extraneous parameters for commands that do not take in parameters (such as `help`, `exit`, `clear`, `bin`, `aliases` and `editprev`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to RosterBolt.

You must be viewing the working list to use this command. Otherwise, an error message will be shown, and the person will not be added.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​ [r/ROLE] [nt/NOTES] [va/AVAILABILITIES]…​ [vr/RECORDS]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags, availabilities, and records (including 0). Role and notes are optional.
</div>

* A person is considered a duplicate if the phone number matches exactly, or the email matches case-insensitively.
  * Email comparisons are case-insensitive across RosterBolt (e.g., `A@b.com` is treated as the same as `a@b.com`).
* `AVAILABILITIES` must be in the format `DAY,HH:mm,HH:mm` (day, start time, end time) where `DAY` is a full day name (case-insensitive, e.g., `MONDAY`, `monday`, or `Monday`) and start time is earlier than end time.
* `RECORDS` must be in the format `yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm` (start date-time, end date-time) and start date-time must be earlier than end date-time.

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 r/Usher nt/Weekend only va/MONDAY,14:00,17:00 vr/2026-03-20T14:00,2026-03-20T17:00`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal r/Logistics nt/Prefers morning shifts va/SATURDAY,09:00,12:00 va/SUNDAY,13:00,16:00`
* `add n/Alex Tan p/91234567 e/alex@example.com a/NUS`

### Listing all persons : `list`

Shows a list of all persons in RosterBolt, optionally sorted by an attribute.

Format: `list [ATTRIBUTE [asc|desc]]`

* Currently supported `ATTRIBUTE`: `name`, `phone`, `email`, `role`, `tag`, or `vr`.
* Order defaults to `asc` when omitted.
* Omitting `ATTRIBUTE` shows the list in the default order.
* `vr` sorts by the end time of each volunteer's most recent record (i.e., `asc` lists least-recently served first, while `desc` lists most-recently served first).
  * Volunteers without records are treated as least-recently served.

Examples:
* `list`
* `list name`
* `list email desc`
* `list vr desc`

### Creating a command alias : `alias`

Creates a custom alias for a supported built-in command word.

Format: `alias SHORT COMMAND_WORD`

* `SHORT` must be a lowercase command-word-style token.
* `COMMAND_WORD` must be exactly one supported built-in command word: `add`, `bin`, `clear`, `delete`, `edit`, `exit`, `export`, `find`, `help`, `import`, `list`, `restore` or `stats`.
* Alias expansion replaces only the leading command word and appends the rest of the user input unchanged.
* `alias`, `aliases`, `unalias`, and `editprev` cannot be used as alias targets.
* Aliases are treated as workflow preferences rather than roster data, so they are persisted in the user preferences file (default: `preferences.json`).
* If invalid aliases are detected in `preferences.json` at startup, they are removed and shown once in the result display.

Examples:
* `alias ls list`
* `alias rm delete`
* `alias wipe clear`

### Listing command aliases : `aliases`

Lists all defined command aliases.

Format: `aliases`

### Removing a command alias : `unalias`

Removes an existing command alias.

Format: `unalias SHORT`

Examples:
* `unalias ls`

### Showing recycle bin of recently deleted persons : `bin`

Shows the recycle bin, which contains all recently deleted persons in RosterBolt.

Format: `bin`

* Persons deleted by the `clear` and `delete` commands will be added to the recycle bin.
* The recycle bin is cleared when the application is closed.

### Editing a person : `edit`

Edits an existing person in RosterBolt.

You must be viewing the working list to use this command. Otherwise, an error message will be shown, and no person will be edited.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [r/ROLE] [nt/NOTES] [t/TAG]…​ [va/AVAILABILITIES]…​ [vr/RECORDS]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, availabilities, or records, existing values of that field will be replaced (i.e. adding is not cumulative).
* You can remove all the person’s tags, availabilities, records, role, or notes by typing `t/`, `va/`, `vr/`, `r/`, or `nt/` without specifying values after the prefix.
* `AVAILABILITY` format: `DAY,HH:mm,HH:mm` where `DAY` is case-insensitive (e.g., `MONDAY,14:00,17:00`).
* `RECORD` format: `yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm` (e.g., `2026-03-20T14:00,2026-03-20T17:00`).

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com va/MONDAY,18:00,20:00` Edits the phone number, email address, and availability of the 1st person.
*  `edit 2 n/Betsy Crower t/ va/ vr/` Edits the name of the 2nd person and clears all existing tags, availabilities, and records.

### Locating persons by keyword: `find`

Finds persons whose fields contain any of the given keywords, optionally filtered by volunteer availability.

Format: `find [m/MATCH_TYPE] [va/DAY,HH:mm,HH:mm] [KEYWORD [MORE_KEYWORDS]]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* The search covers all fields: name, phone, email, address, role, notes, and tags.
* `m/kw` matches full words only. e.g. `Han` will not match `Hans`
* `m/ss` matches substrings. e.g. `Han` will match `Hans`
* `m/fz` allows small spelling mistakes. Words that are up to 2 simple edits away (in terms of adding, removing, or changing a letter) can still match. e.g. `michigan` will match `michegan`
* `va/DAY,HH:mm,HH:mm` filters for volunteers whose availability covers the specified time period, i.e. the volunteer's availability is on the same day, starts at or before the specified start time, and ends at or after the specified end time. `DAY` is a full day name (case-insensitive, e.g., `MONDAY`), and start time must be before end time.
* At least one of keywords or `va/` must be provided.
* When both keywords and `va/` are provided, only persons matching **both** the keyword search **and** the availability filter are returned.
* If `m/MATCH_TYPE` is specified, at least one keyword must also be provided.
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
* `find va/MONDAY,14:00,17:00` returns all persons available on Monday from 14:00 to 17:00
* `find va/MONDAY,14:00,17:00 alice` returns persons matching `alice` who are also available on Monday from 14:00 to 17:00

### Viewing volunteer statistics : `stats`

Displays volunteer statistics as text-based charts in the result display.

Format: `stats CATEGORY`

* Currently supported `CATEGORY`: `role`, `record`.
* `role` shows the percentage distribution of volunteer roles (empty roles are shown as `Unassigned`).
* `record` shows volunteers ranked by the number of volunteer records.

Examples:
* `stats role`
* `stats record`

### Deleting a person : `delete`

Deletes the specified persons from RosterBolt.

You must be viewing the working list to use this command. Otherwise, an error message will be shown, and no persons will be deleted.

Format: `delete INDEX [MORE_INDICES]`

* Deletes the person at the specified indices.
* Indices refer to index numbers shown in the displayed person list.
* Indices **must be positive integers** 1, 2, 3, …​
* Duplicate indices will be ignored.
* Deleted persons will be added to the recycle bin.

Examples:
* `list` followed by `delete 2 3` deletes the 2nd and 3rd persons in RosterBolt.
* `list` followed by `delete 3 3 2` has the same behavior, as duplicate indices are ignored and the order of indices does not matter.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Restoring a deleted person : `restore`

Restores the specified persons from RosterBolt.

You must be viewing the recycle bin to use this command. Otherwise, an error message will be shown, and no persons will be restored.

Format: `restore INDEX [MORE_INDICES]`

* Restores the person at the specified indices.
* Indices refer to index numbers shown in the displayed recycle bin.
* Indices **must be positive integers** 1, 2, 3, …​
* Duplicate indices will be ignored.
* Restored persons will be removed from the recycle bin and added to the working list of kept contacts.
* Persons are considered duplicate if their phone matches exactly, or email matches case-insensitively.
  * You cannot restore persons who are duplicates of existing contacts in the working list.
  * You cannot restore two persons who are duplicates of each other in the recycle bin.

Examples:
* `bin` followed by `restore 2 3` restores the 2nd and 3rd persons in the recycle bin.
* `bin` followed by `restore 3 3 2` has the same behavior, as duplicate indices are ignored and the order of indices does not matter.

### Importing volunteers from a CSV file : `import`

Imports volunteers from a CSV file into the active address book.

Format: `import FILE_PATH`

Examples:
* `import data/volunteers.csv`

### Exporting volunteers to a CSV file : `export`

Exports all active volunteers in the address book to a CSV file.

Format: `export FILE_PATH`

Examples:
* `export data/volunteers.csv`

### Clearing all entries : `clear`

Clears all entries from RosterBolt.

You must be viewing the working list to use this command. Otherwise, an error message will be shown, and your contacts will not be cleared.

Format: `clear`

* Deleted persons will be added to the recycle bin.

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Editing the previous command : `editprev`

Loads the last successfully executed command, excluding `editprev`, into the command box so you can edit and run it again.

Format: `editprev`

* Only the last successful non-`editprev` command is remembered for the current session.
* `editprev` does not execute the recalled command. You can change it first before pressing Enter.

Examples:
* `list` followed by `editprev` loads `list` back into the command box.
* `delete 1` followed by `editprev` loads `delete 1` back into the command box for editing.

### Saving the data

RosterBolt data are saved to the hard disk automatically after any command that changes the data. There is no need to save manually.

If a command is executed successfully but RosterBolt fails to save the updated data to the hard disk, the result display will still show the command’s success message, together with an error message explaining that saving failed and a warning that the changes are not saved and will be lost if the application is closed.

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
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​ [r/ROLE] [nt/NOTES] [va/AVAILABILITIES]…​ [vr/RECORDS]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague r/Usher nt/Available weekends va/SUNDAY,14:00,17:00 vr/2026-03-20T14:00,2026-03-20T17:00`
**Alias** | `alias SHORT COMMAND_WORD`<br> e.g., `alias ls list`
**Aliases** | `aliases`
**Unalias** | `unalias SHORT`<br> e.g., `unalias ls`
**Bin** | `bin`
**Clear** | `clear`
**Delete** | `delete INDEX [MORE_INDICES]`<br> e.g., `delete 2 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [r/ROLE] [nt/NOTES] [t/TAG]…​ [va/AVAILABILITY]…​ [vr/RECORD]…​`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com va/MONDAY,14:00,17:00`
**Edit Previous** | `editprev`
**Export** | `export FILE_PATH`<br> e.g., `export data/volunteers.csv`
**Find** | `find [m/MATCH_TYPE] [va/DAY,HH:mm,HH:mm] [KEYWORD [MORE_KEYWORDS]]`<br> e.g., `find m/kw James Jake`, `find m/ss ali`, `find m/fz michigan`, `find va/MONDAY,14:00,17:00`, `find va/MONDAY,14:00,17:00 alice`
**Import** | `import FILE_PATH`<br> e.g., `import data/volunteers.csv`
**List** | `list [ATTRIBUTE [asc|desc]]`<br> e.g., `list name desc`, `list vr desc`
**Exit** | `exit`
**Help** | `help`
**Restore** | `restore INDEX [MORE_INDICES]`<br> e.g., `restore 2 3`
**Stats** | `stats CATEGORY`<br> e.g., `stats role`, `stats record`
