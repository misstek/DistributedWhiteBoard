# DistributedWhiteBoard
Shared whiteboards allow multiple users to draw simultaneously on a canvas.  
Shared whiteboards should support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text. 
## How to use
Set up server first  
```
java -jar WhiteBoardServer.jar <serverPort>
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
## Interaction Diagram
