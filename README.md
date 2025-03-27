# QA Project
## Overview
The QA Project is a command-line-based Q&A platform built in Java. It allows users to sign up, log in, and interact with a question-and-answer system. Users can create, view, edit, and delete questions and answers.
## Prerequisites
Ensure you have the following installed:

- Java 8 or later

- Apache Maven

## Installation
Clone the repository and navigate to the project folder:
```
git clone https://github.com/your-username/QA-Project.git
cd QA-Project
```

## Running the Project

### Start the Server

Run the following command to start the server:
```
mvn exec:java@run-server
```
Start a User Session
```
mvn exec:java@run-user
```
### User Authentication
When starting a user session, you will see:
```
Do you want to sign in or sign up? (Write sign in or sign up)
```
Enter ```sign in``` if you already have an account, or ```sign up``` to create a new one.
- Username: Enter your username (e.g., ```test1```).

- Password: Enter your password (e.g., ```test1```).

If the credentials are invalid, you will be prompted again.

## Available Commands
Once signed in, you will see the list of available commands:
```
------------List of Commands------------
・List Questions
・View Question
・Create Question
・Create Answer
・Show Answers
・Edit question
・Edit answer
・Delete question
・Delete answer
・exit
```
## Example Usage
We suppose that we cannot have mutitle same question title contents, and Users only answer once a question.
If you want to edit it, use edit command.

- List questions:
```
list questions
```
- View question:
```
view question
```
Write you want to view a question.
For example, ```demo1```
If you want to answer it, write ```yes```, otherwise ```no```
If you type the answer, type ```END`` at the end.
- Create a new question:
```
create question
```

Type your question and finish by entering:
```
END
```
- Answer a question:
```
create answer
```
Provide the question title and then your answer, ending with:
```
END
```
- Show answers to a question:
```
show answers
```
Type the questions you want to see the answers for.
- Edit a question or answer:
```
edit question
```

```
edit answer
```
Type the question you want to edit.
If you want to edit it, type ```yes```, otherwise ```no```.
Modify the text and finish with:
```
END
```
- Delete a question or answer:
```
delete question
```
```
delete answer
```
Type ```yes``` if you really want to delete it, otherwise ```no```.
If you are the author, the item will be deleted.

- Exit the session:
```
exit
```
