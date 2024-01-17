# Water Flow Simulation Project 

**Overview**

This project simulates the ﬂow of water over a terrain in a graphical
environment using Java, with capabilities for user-interaction.
<br>
<br>

**Features**

> ● Terrain Generation: Converts terrain data to greyscale image.
>
> ● Water Simulation: Interactive simulation of water ﬂow over the
> terrain.
>
> ● Mouse Interaction: Add water to the terrain using mouse
> clicks.
>
> ● Multithreading: Eﬃcient water ﬂow calculations using multithreading.
<br>

**Classes**

> ● Terrain: Acts as the Model part of the design, managing important
> terrain data and making important calculations that dictate the ﬂow of
> water.
>
> ● Water: Handles the data and logic for water storage over the
> terrain, with methods for updating the data.
>
> ● Flow: Responsible for handling the GUI, and starting and joining
> threads.
>
> ● WaterClickListener: Processes mouse click events for adding water to
> the terrain interactively.
>
> ● FlowPanel: Responsible for painting the terrain.
>
> ● Control: Controls the running of the threads over the permuted
> lists.
<br>

**Prerequisites**

> ● Java Development Kit (JDK) 8 or later.
<br>

**Setup** **and** **Running** **the** **Application**

> 1\. Clone the repository or download the project to your local
> machine.
>
> 2\. Open the project in your terminal.
>
> 3\. To compile the code, type \"make\", making sure you are in the
> root directory of the project.
>
> 4\. To run the application, make sure
> you are still in the root directory of the project, then type \"make
> run\" to run the application with the default terrain data. If you
> want to run the application with your own data, type \"make run
> myvar=ﬁle_path\".
<br>

**Usage**

> ● On application launch, the terrain will be displayed in a window.
>
> ● Use the mouse to click on the terrain to add water at that location.
>
> ● Click "play" to watch the simulation of water ﬂowing over the
> terrain. You can also stop, start, and reset the water ﬂow at your
> leisure.
<br>

**Contributions**

Contributions are welcome. Please adhere to the project\'s coding
standards and submit pull requests for any proposed changes.
<br>
<br>

**License**

This project is licensed under the MIT License - see the
[LICENSE](https://chat.openai.com/c/LICENSE) ﬁle for details.
