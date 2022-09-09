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
        String action = ""; // the action to be performed

        action = puzzleSolutionsDictionary.keySet().stream()
                .filter(key -> puzzleSolutionsDictionary.get(key).contains(command)).findFirst().orElse(null);

        if (action != null) {
            return action;
        }

        // if the command consists of an object, it is returned as it is
        // N.B. a command consisting of a single object is to be accepted
        // only when waiting for the second part of an action

        action = nounDictionary.keySet().stream().filter(key -> nounDictionary.get(key).contains(command)).findFirst()
                .orElse(null);

        if (action != null) {
            return action;
        }

        // if the command can be traced back to a complete action,
        // the standard command is returned

        action = completeActionsDictionary.keySet().stream()
                .filter(key -> completeActionsDictionary.get(key).contains(command)).findFirst().orElse(null);

        if (action != null) {
            return action;
        }

        // if the command can be traced back to an incomplete action,
        // the command is further analyzed to parse the action and the objects

        String firstNoun = "";
        String secondNoun = "";

        String newCommand = ""; // the command is modified multiple times, so it is stored in a new variable

        for (String key : incompleteActionsDictionary.keySet()) { // for each action in the dictonary

            String tmpString = incompleteActionsDictionary.get(key).stream()
                    .filter(string -> command.startsWith(string))
                    .findFirst().orElse(null);

            if (tmpString != null) {
                action = key;
                newCommand = command.replace(tmpString, "");
                newCommand = newCommand.trim();

                break;
            }

        }

        // if the action is still not found, the command is returned as "fail"
        if (action == null)
            return "fail";

        switch (action) {
            case "Spostati":
                // expects the direction
                newCommand.trim();
                if (newCommand.matches(directionPattern)) {
                    firstNoun = newCommand;
                    newCommand = "";
                }
                break;

            case "Raccogli":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";
                newCommand = clearCommand(newCommand, firstNoun);

                break;

            case "Raggiungi":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";

                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Usa":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";
                newCommand = clearCommand(newCommand, firstNoun);

                break;

            case "Cerca":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";
                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Esamina":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";
                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Getta":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";
                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Consegna":
                // expects a noun
                firstNoun = lookForNoun(newCommand);

                if (firstNoun.equals("fail"))
                    return "fail";
                newCommand = clearCommand(newCommand, firstNoun);
                break;

            case "Svuota":
                if (newCommand.contains("brocca")) { // the command involves moving a jug

                    // recognize the first jug

                    if (newCommand.startsWith("la prima brocca")) {
                        firstNoun = "brocca 1";
                        newCommand = newCommand.replaceFirst("la prima brocca", "");
                    } else if (newCommand.startsWith("la seconda brocca")) {
                        firstNoun = "brocca 2";
                        newCommand = newCommand.replaceFirst("la seconda brocca", "");
                    } else if (newCommand.startsWith("la terza brocca")) {
                        firstNoun = "brocca 3";
                        newCommand = newCommand.replaceFirst("la terza brocca", "");
                    } else
                        return "fail";

                    newCommand = newCommand.trim();

                    // the second jug must be preceded by the preposition "nella"
                    if (newCommand.startsWith("nella")) {
                        // recognize the second jug

                        if (newCommand.startsWith("nella prima brocca")) {
                            secondNoun = "brocca 1";
                            newCommand = newCommand.replaceFirst("nella prima brocca", "");
                        } else if (newCommand.startsWith("nella seconda brocca")) {
                            secondNoun = "brocca 2";
                            newCommand = newCommand.replaceFirst("nella seconda brocca", "");
                        } else if (newCommand.startsWith("nella terza brocca")) {
                            secondNoun = "brocca 3";
                            newCommand = newCommand.replaceFirst("nella terza brocca", "");
                        } else
                            return "fail";
                    } else
                        return "fail";
                }

                break;

            case "Attacca":

                if (newCommand.startsWith("con")) { // the command introduces the weapon with "con"
                    newCommand = newCommand.replaceFirst("con", "");
                    newCommand = newCommand.trim();
                    System.out.println(newCommand);
                    firstNoun = lookForNoun(newCommand);
                    if (firstNoun.equals("fail"))
                        return "fail";
                    newCommand = clearCommand(newCommand, firstNoun);
                    break;
                }

            default:
                return "fail";
        }

        if (secondNoun != "") {
            return action + " " + firstNoun + "+" + secondNoun;
        } else {
            return action + " " + firstNoun;
        }

    }

    private static String lookForNoun(String newCommand) {

        String noun = nounDictionary.keySet().stream()
                .filter(key -> nounDictionary.get(key).contains(newCommand))
                .findFirst()
                .orElse(null);
        if (noun != null)
            return noun;
        else
            return "fail";

    }

    private static String clearCommand(String newCommand, String firstNoun) {

        for (String string : nounDictionary.get(firstNoun)) { // for each possible alternative for the key
            if (newCommand.startsWith(string)) { // if the command start with the alternative,
                // an acceptable noun is found
                return newCommand.replaceFirst(string, ""); // the noun is removed
            }
        }
        return newCommand;
    }

}
