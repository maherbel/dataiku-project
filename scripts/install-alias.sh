#!/bin/bash

# Remove existing link/execs
rm -f /usr/local/bin/give-me-the-odds
rm -f give-me-the-odds

# Get the path to the executable jar file from the command-line arguments
# name_of_executable_war_file=$1
name_of_executable_war_file="millenium-0.0.1-SNAPSHOT.war"

# Create the shell script
echo "#!/bin/bash

# Get the path to the current script
path_to_shell_script=$(dirname "$0")

# Get the path to the JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
  path_to_java_home=$(dirname "$(which java)")
else
  path_to_java_home=$JAVA_HOME
fi

# Set up the environment
export JAVA_HOME=$path_to_java_home

# Run the executable jar file with the required arguments
java -jar $name_of_executable_war_file \$1 \$2" > give-me-the-odds

# Make the shell script executable
chmod +x give-me-the-odds

# Create a symbolic link to the shell script
ln -s $path_to_shell_script/give-me-the-odds /usr/local/bin/

# Commands available with
# ./give-me-the-odds example1/millennium-falcon.json example1/empire.json
# ./give-me-the-odds example2/millennium-falcon.json example2/empire.json
# ./give-me-the-odds example3/millennium-falcon.json example3/empire.json
# ./give-me-the-odds example4/millennium-falcon.json example4/empire.json