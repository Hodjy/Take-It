# Take-It!
![image](https://user-images.githubusercontent.com/62711261/137179319-60fbf89c-528b-467a-98f0-e074d24c0acd.png)         
An Application for handing over items for free.         
Project uses MVVM (view model, and mutablelivedata).
Using Firebase Realtime Database for item storage, Firebase auth for user log-in, and Firebase Messaging for user-to-user message notifications.        
            
                
## Catalog
![Catalog](https://user-images.githubusercontent.com/62711261/137192553-75494ec9-3476-4861-bade-0e2756ed1df6.gif)           
Showcasing all the items in the database, realtime updates when users upload new items.                   
Using a nested recycleview in a recycleview.    
        
          
## Log in and Register        
![Register](https://user-images.githubusercontent.com/62711261/137292234-52720564-8261-4d60-a33c-d6c8182139b2.gif)
![ChangeProfilePic](https://user-images.githubusercontent.com/62711261/137293164-ba482d3c-602b-4315-9b40-2877a15b1d24.gif)        
User can easily register in app. Firebase Auth is used for this part along with realtime database for custom-data creation and storage.         
He can then change his profile picutre at the profile section.
            
          
                
![SignIn](https://user-images.githubusercontent.com/62711261/137292255-8b79f6e4-1415-47c2-aa81-95773a6fcc3d.gif)
![GuestLogin](https://user-images.githubusercontent.com/62711261/137292449-56c18b09-4fe8-4c6c-bcbe-5a983dcc693c.gif)          
User can either sign in with an account, or log in as a guest, though he will have limited options that way.        
                
          
          
## Items
![image](https://user-images.githubusercontent.com/62711261/137290814-bd05d21d-7a14-4d86-b69e-fb76526afcf9.png)           
Tapping an item will show its description.        
        
![UploadItem](https://user-images.githubusercontent.com/62711261/137296271-c4d9ebff-d776-44eb-b889-5b4c1d0a8353.gif)        
A logged-in user can create and upload a new item to the database, which will update in realtime for other users.         

![image](https://user-images.githubusercontent.com/62711261/137291031-6cbae71c-7170-43b2-b0fe-d1e7df3bfaaa.png)
![DeleteItem](https://user-images.githubusercontent.com/62711261/137292040-d0b967fa-23b3-4229-a88f-d7ff03e6e4d6.gif)          
At the user profile, a highlight of user-owned items are showcased.         
Pressing an owned item will show the details as usual, along with a FAB for item deletion.          
Deleting an item will remove it entierly from the database, and update realtime for the users.          


## User to User Interaction       
![XboxMsgNew1](https://user-images.githubusercontent.com/62711261/137302665-2bb1a21d-33ed-4a14-af77-0d26c4cfb782.gif)      
User can enter an item description in order to send a message requesting that item from another user.       
            
![XboxNotificationNew](https://user-images.githubusercontent.com/62711261/137302689-7e569281-ff54-43cb-a699-c569c0dfe03d.gif)
![XboxMsgingNew](https://user-images.githubusercontent.com/62711261/137302717-52bf50b1-f418-4122-8e3e-9a4480fb9cdf.gif)                                                 
If the user left the app and receives a message, he will receive a notification with the message contents and the sender's name.        
The chatrooms with unread messages will be highlighted in a different color than the other chatrooms.
Messaging are realtime and are handeld by the realtime database.

