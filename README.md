# Paytm
SessionizeElb

=======================================

# Notes  :
1. Unique User = ClientIP + User Agent (Excluded port number from the Client IP - as same Ip can have multiple ports)
2. Session Time = (Max Timestamp - Min Timestamp) --- in the window of 15 mins for each user.
3. Excluded microSeconds from the Timestamp
4. The "interval" field in the output contains the Start Time for the 15 mins interval for all records in that interval.

=======================================

# Scenarios Handled

1. Handling Double Quoted Fields in the Text data.

2. Some records did not have even number of Double quotes - Hence Ignored.
 
   Example : 2015-07-22T16:10:50.873283Z marketpalce-shop 106.51.132.54:4210 10.0.6.99:81 0.000024 0.000215 0.00002 301 301 0 178 "GET http://paytm.com:80/%27"()&%251%3CScRiPt%20%3Eprompt(981045)%3C/ScRiPt%3E/about HTTP/1.1" "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)" - -

=====================================
# Run Instructions

The code expects 4 inputs - 
1. Input file path on HDFS
2. Window Duration in Seconds
3. Output Path for Per Window, Per User aggregation - Gives Session Duration and Distinct Req Count
4. Output Path for Per User aggregation - Avg Session, Total Session time- Sorted in Desc Order.