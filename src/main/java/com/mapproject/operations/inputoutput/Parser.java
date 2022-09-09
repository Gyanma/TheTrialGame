package com.mapproject.operations.inputoutput;

import java.util.List;
import java.util.Map;

import com.mapproject.operations.runHandler.Loader;

public class Parser {
    private static Map<String, List<String>> nounDictionary;
    private static Map<String, List<String>> completeActionsDictionary;
    private static Map<String, List<String>> incompleteActionsDictionary;
    private static Map<String, List<String>> puzzleSolutionsDictionary;
    private static String directionPattern = "(nord)|(sud)|(est)|(ovest)";

    public Parser() {
        nounDictionary = Loader.loadDictionary("nounDictionary");
        completeActionsDictionary = Loader.loadDictionary("completeActionsDictionary");
        incompleteActionsDictionary = Loader.loadDictionary("incompleteActionsDictionary");
        puzzleSolutionsDictionary = Loader.loadDictionary("puzzleSolutionsDictionary");
    }

    public static String parseCommand(String command) {

        // if the command can be traced to a correct puzzle
        // solution, the standard solution is returned
        for (String key : puzzleSolutionsDictionary.keySet()) { // for each solution in the dictionary
            if (puzzleSolutionsDictionary.get(key).contains(command)) { // if the command is a solution
                return key; // return the standard solution
            }
        }

        // if the command consists of an object, it is returned as it is
        // N.B. a command consisting of a single object is to be accepted
        // only when waiting for the second part of an action
        for (String key : nounDictionary.keySet()) { // for each object in the dictionary
            if (nounDictionary.get(key).contains(command)) { // if the command is an object
                return key; // return the object
            }
        }

        // if the command can be traced back to a complete action,
        // the standard command is returned
        for (String key : completeActionsDictionary.keySet()) { // for each action in the dictionary
            if (completeActionsDictionary.get(key).contains(command)) { // if the command is an action
                return key; // return the standard command
            }
        }

        // if the command can be traced back to an incomplete action,
        // the command is further analyzed to parse the action and the objects

        boolean sentenceFound = false;
        String action = ""; // the action to be performed
        // some commands may involve 1 or 2 elements
        String firstNoun = "";
        String secondNoun = "";

        String newCommand = ""; // the command is modified multiple times, so it is stored in a new variable

        for (String key : incompleteActionsDictionary.keySet()) { // for each action in the dictonary
            for (String string : incompleteActionsDictionary.get(key)) { // for each alternative name of the action
                if (command.startsWith(string)) {
                    action = key;
                    sentenceFound = true;

                    newCommand = command.replace(string, "");
                    newCommand = newCommand.trim();

                    break;
                }
            }
        }

        // if the command is not correctly formed, the command is returned as "fail",
        // else the boolean is reset to allow further checks
        if (!sentenceFound) {
            return "fail";
        } else
            sentenceFound = false;

        switch (action) {
            case "Spostati":
                // expects the direction
                if (newCommand.matches(directionPattern)) {
                    newCommand.trim();
                    firstNoun = newCommand;
                    sentenceFound = true;
                    newCommand = "";

                }
                break;

            case "Raccogli":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;
                newCommand = clearCommand(newCommand, firstNoun);

                break;

            case "Raggiungi":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;

                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Usa":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;
                newCommand = clearCommand(newCommand, firstNoun);

                break;

            case "Cerca":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;
                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Esamina":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;

                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Getta":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;

                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Consegna":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (!firstNoun.equals("fail"))
                    sentenceFound = true;
                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Svuota":
                if (newCommand.contains("brocca")) { // the command involves moving a jug

                    // recognize the first jug

                    if (newCommand.startsWith("la prima brocca")) {
                        firstNoun = "brocca 1";
                        newCommand = newCommand.replaceFirst("la prima brocca", "");
                        sentenceFound = true;
                    } else if (newCommand.startsWith("la seconda brocca")) {
                        firstNoun = "brocca 2";
                        newCommand = newCommand.replaceFirst("la seconda brocca", "");
                        sentenceFound = true;
                    } else if (newCommand.startsWith("la terza brocca")) {
                        firstNoun = "brocca 3";
                        newCommand = newCommand.replaceFirst("la terza brocca", "");
                        sentenceFound = true;

                    } else
                        sentenceFound = false;

                    newCommand = newCommand.trim();

                    // the second jug must be preceded by the preposition "nella"
                    if (sentenceFound && newCommand.startsWith("nella")) {
                        // recognize the second jug

                        if (newCommand.startsWith("nella prima brocca")) {
                            secondNoun = "brocca 1";
                            newCommand = newCommand.replaceFirst("nella prima brocca", "");
                            sentenceFound = true;
                        } else if (newCommand.startsWith("nella seconda brocca")) {
                            secondNoun = "brocca 2";
                            newCommand = newCommand.replaceFirst("nella seconda brocca", "");
                            sentenceFound = true;
                        } else if (newCommand.startsWith("nella terza brocca")) {
                            secondNoun = "brocca 3";
                            newCommand = newCommand.replaceFirst("nella terza brocca", "");
                            sentenceFound = true;
                        } else
                            sentenceFound = false;

                    }
                } else // if no jug is found, the command is invalid
                    sentenceFound = false;
                break;

            case "Attacca":
                // first possibility: the command only specifies the weapon

                if (newCommand.startsWith("con")) { // the command introduces the weapon with "con"
                    newCommand = newCommand.replaceFirst("con", "");
                    newCommand = newCommand.trim();
                    firstNoun = lookForNoun(newCommand);
                    if (!firstNoun.equals("fail"))
                        sentenceFound = true;
                    newCommand = clearCommand(newCommand, firstNoun);

                }

            default:
                break;
        }

        if (!sentenceFound || (sentenceFound && newCommand.length() > 0))
            return "fail";
        else if (secondNoun != "") {
            return action + " " + firstNoun + "+" + secondNoun;
        } else {
            return action + " " + firstNoun;
        }

    }

    private static String lookForNoun(String newCommand) {

        for (String key : nounDictionary.keySet()) { // for each key in the dictionary
            if (nounDictionary.get(key).contains(newCommand)) { // if the command contains the key
                return key; // return the key
            }
        }
        return "fail";

    }

    private static String clearCommand(String newCommand, String firstNoun) {

        if (firstNoun != "fail") {
            for (String string : nounDictionary.get(firstNoun)) { // for each possible alternative for the key

                if (newCommand.startsWith(string)) { // if the command start with the alternative,
                    // an acceptable noun is found

                    return newCommand.replaceFirst(string, ""); // the noun is removed

                }
            }
        }

        return newCommand;
    }

}
