# DistributedWhiteBoard
Shared whiteboards allow multiple users to draw simultaneously on a canvas.  
Shared whiteboards should support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text. 
## How to use
Set up server first  
```
java -jar WhiteBoardServer.jar <serverIPAddress> <serverPort>
```
The manager should join first  
```
java -jar CreateWhiteBoard.jar <serverIPAddress> <serverPort> username
```
User client join white board
```
java -jar JoinWhiteBoard.jar <serverIPAddress> <serverPort> username
```
## Key Features
1. All the peers will see the identical image of the whiteboard, as well as have the privilege of doing all the operations.  
2. Online peers can choose to leave whenever they want. The manager can kick someone out at any time.  
3. When the manager quits, the application will be terminated. All the peers will get a message notifying them.  
4. An online peer list should be maintained and displayed  
### Manager View
![image](https://github.com/misstek/DistributedWhiteBoard/blob/master/Img/manager%20view.png)
  
The manager can kick out a certain peer/user  
Only the manager of the whiteboard should be allowed to create a new whiteboard, open a previously saved one, save the current one, and close the application.  
### User View
![image](https://github.com/misstek/DistributedWhiteBoard/blob/master/Img/user%20view.png)
  
All the users should see the same image of the whiteboard and should have the privilege of doing all the drawing operations.  


## Interaction Diagram
![image](https://github.com/misstek/DistributedWhiteBoard/blob/master/Img/Interaction.png)
