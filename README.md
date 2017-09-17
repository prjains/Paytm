# Paytm
SessionizeElb


=======================================

#Assumptions :
1. Taken a window of 15 mins for aggregation.
2. Unique User = ClientIP + User Agent (Excluded port number from the Client IP - as same Ip can have multiple ports)
2. Session Time = (Max Timestamp - Min Timestamp) --- in the window of 15 mins for each user.
4. Excluded microSeconds from the Timestamp

=======================================
