package at.vibes.libMinecraft.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandArgs {
    private final BaseCommand baseCommand;
    private final String baseLabel;
    private final SubCommand subCommand;
    private final String label;
    private final Set<String> flags;
    private final List<String> params;
    private final boolean empty;
    private final int size;

    public CommandArgs(BaseCommand baseCommand, String baseLabel, SubCommand subCommand, String[] args) {
        this.baseCommand = baseCommand;
        this.baseLabel = baseLabel;
        this.subCommand = subCommand;
        this.flags = new HashSet<>();
        this.params = new ArrayList<>();

        if (args.length > 0) {
            this.label = args[0];

            char firstChar;
            char quoteChar = '\0';
            int length;
            StringBuilder quotedArgBuilder = null;

            for (int i = 1; i < args.length; ++i) {
                firstChar = args[i].charAt(0);
                length = args[i].length();

                if (length < 1) {
                    if (quotedArgBuilder != null) {
                        quotedArgBuilder.append(' ');
                    }
                    continue;
                }

                switch (firstChar) {
                    case '\'':
                    case '"':
                        if (quotedArgBuilder == null) {
                            if (i + 1 >= args.length) {
                                this.params.add(args[i].substring(1));
                            }else {
                                quoteChar = firstChar;
                                quotedArgBuilder = new StringBuilder(args[i].substring(1));
                            }
                            break;
                        }
                    case '-':
                        if (quotedArgBuilder == null && args[i].matches("^\\-[A-Za-z]+$")) {
                            this.flags.add(args[i].substring(1));
                            break;
                        }
                    default:
                        if (quotedArgBuilder == null) {
                            this.params.add(args[i]);
                        }
                        else
                        {
                            int quoteOffset = args[i].indexOf(quoteChar);
                            if (quoteOffset >= 0) {
                                String before = args[i].substring(0, quoteOffset);
                                String after = "";
                                if (quoteOffset + 1 < length) {
                                    after = args[i].substring(quoteOffset + 1);
                                }

                                if (before.length() > 0) {
                                    quotedArgBuilder.append(' ').append(before);
                                }
                                this.params.add(quotedArgBuilder.toString());
                                quotedArgBuilder = null;

                                if (after.length() > 0) {
                                    this.params.add(after);
                                }
                            }else {
                                quotedArgBuilder.append(' ').append(args[i]);
                                if (i + 1 >= args.length) {
                                    this.params.add(quotedArgBuilder.toString());
                                    quotedArgBuilder = null;
                                }
                            }
                        }
                }
            }
        }else {
            throw new IllegalArgumentException("There need to be at least 1 argument!");
        }
        this.empty = this.params.isEmpty();
        this.size = this.params.size();
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public int size() {
        return this.size;
    }

    public BaseCommand getBaseCommand()
    {
        return this.baseCommand;
    }

    public SubCommand getSubCommand() {
        return this.subCommand;
    }

    public String getLabel() {
        return this.label;
    }

    public String getBaseLabel() {
        return this.baseLabel;
    }

    public boolean hasFlag(String flag) {
        return this.flags.contains(flag);
    }

    public boolean hasFlags(String... flags) {
        for (String flag : flags) {
            if (!this.hasFlag(flag)) {
                return false;
            }
        }
        return true;
    }

    public String getString(int i) {
        return this.params.get(i);
    }

    public String getString(int i, String def) {
        if (i >= 0 && this.size > i) {
            return this.params.get(i);
        }
        return def;
    }

    public int getInt(int index) throws NumberFormatException {
        return Integer.parseInt(this.getString(index));
    }

    public double getDouble(int index) throws NumberFormatException {
        return Double.parseDouble(this.getString(index));
    }

    public long getLong(int index) throws NumberFormatException {
        return Long.parseLong(this.getString(index));
    }

    public boolean getBoolean(int index) {
        return this.getBoolean(index, "true", "yes", "on", "1", "enable");
    }

    public boolean getBoolean(int index, String... trueWords) {
        String string = this.getString(index);
        for (String word : trueWords) {
            if (string.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getFlags() {
        return Collections.unmodifiableSet(this.flags);
    }

    public List<String> getParams() {
        return Collections.unmodifiableList(this.params);
    }
}