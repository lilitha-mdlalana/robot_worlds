# 🌟✨🤖 Robot World 🌟🤖✨

## ✨Welcome to the Amazing Robot World!✨

#### Explore a virtual realm where robots roam freely amidst towering mountains, shimmering lakes, and mysterious bottomless pits. Embark on an adventure where every command you issue shapes the destiny of these mechanical marvels. Are you ready to unleash your creativity and command robots in this dynamic and interactive simulation?


### Setup

#### Running the Server

1. **Navigate to the Server Directory**: Open your terminal and change directory to where your server code is located.

2. **Execute the Server**: Use the following command to run the server:
   `./runServer.sh`

#### Running the Client

1. **Navigate to the Client Directory**: Open your terminal and change directory to where your server code is located.

2. **Execute the Client**: Use the following command to run the server:
   `./runClient.sh`




### How to Play

1. **Launching a Robot**

   Command: `launch <robot_model> <robot_name>`  
   Description: Start the game by launching a robot into the world with a unique name.

2. **Issuing Commands**

   Commands Available:
   - `forward <number>`: Move the robot forward by a specified number of steps.
   - `back <number>`: Move the robot backward by a specified number of steps.
   - `turn left`: Rotate the robot 90 degrees counterclockwise.
   - `turn right`: Rotate the robot 90 degrees clockwise.
   - `look`: Gather information about the surroundings up to a specified distance.
   - `fire`: Use the robot's firepower strategically.
   - `reload`: Reload the robot's ammunition.
   - `repair`: Repair the robot's damage.

3. **Exploring the World**

   Description: Use the `look` command to scan and gather information about nearby obstacles, other robots, and potential threats.

4. **Engaging in Combat**

   Command: `fire`  
   Description: Strategically use the `fire` command to disable other robots and gain a tactical advantage in the game.

5. **Winning**

   Objective: Navigate your robot successfully through the world, avoid obstacles, engage effectively in combat, and achieve dominance to win the game.



### Game Rules

1. **Robot Commands**

   - You can issue commands to robots such as `forward`, `back`, `turn left`, `turn right`, `look`, and `fire`.

2. **Obstacles**

   - Robots may encounter obstacles like mountains, lakes, and bottomless pits that restrict their movement.

3. **World Exploration**

   - Use the `look` command to survey the surroundings within a specified radius.

4. **Combat**

   - Robots can engage in combat using the `fire` command, aiming to disable other robots.

5. **Objective**

   - The goal is to navigate the world, avoid obstacles, and outmaneuver opponents to dominate the robot world.

### Server
1. **Server Commands**

   Commands Available:
* `quit` : Disconnects all robots and ends the world
* `robots` : Lists all robots including the robot's name and state
* `view` : displays all the available/acceptable commands
* `dump` : Displays a representation of the worlds state
* `save` : Saves the world state
* `restore` : Restore the world state

2. **Restore command**

This command restores the world state(World size and Obstacles) to one of the states saved in the database
3. **Save command**

This command saves the world state(World size and Obstacles) in a database

