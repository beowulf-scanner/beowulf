#!/usr/bin/python
# -*- coding: utf-8 -*-


import httplib
import urllib
import sys

def main():
  if (len(sys.argv) < 2):
    print "[*] Usage: python %s <server ip> <path_to_config_file>" % (sys.argv[0])
    sys.exit(0)
  f = open(sys.argv[2])

  contentbody = f.read()
  f.close()
  print "[*] Rest Client for Beowulf scanner"
  
  headers = { "Content-type" : "application/xml",
	      "Accept" : "application/xml" }
  conn = httplib.HTTPConnection(sys.argv[1]+":13000")
  y = raw_input("Submit scan (y/n): ")
  if(y=='y'):
    print "[*] Scan submitted"
    conn.request("POST","/api/scan/new",contentbody,headers)
    response = conn.getresponse()
    print_response(response)
    h = response.getheaders()
    for x in range(0,len(h)):
      if(h[x][0]=='location'):
	l = h[x][1]  
    scan_id = l.split('/')[4] #hack
  else:      
    scan_id = raw_input("Enter scan id: ")

  while(True):
    try:
    
      print_options()
      a = raw_input("Enter your option: ")
      if(int(a) == 1):
	scan_status_query(scan_id, conn, headers)
      elif(int(a) == 2):
	query_issuetypes(scan_id, conn, headers)
      elif(int(a) == 3):
	query_issues(scan_id, conn, headers)
      elif(int(a) == 4):
	query_single_issuetype(scan_id, conn, headers)
      elif(int(a) == 5):
	query_single_issue(scan_id, conn, headers)      
      elif(int(a) == 6):
	abort_scan(scan_id, conn, headers)
      elif(int(a) == 7):
	scan_summary(scan_id, conn, headers)
      elif(int(a) == 8):
	scan_config(scan_id, conn, headers)
      elif(int(a) == 9):
	scan_report(scan_id, conn, headers)
      elif(int(a) == 10):
	pause_scan(scan_id, conn, headers)
      elif(int(a) == 11):
	gen_config(conn, headers)
      elif(int(a)== 0):
	print "Bye"
	sys.exit()
    except Exception as e:
      print "Exception caught: %s" % e
      pass
    
def print_options():
  print "[*] Available Options"
  print "  [+] 0 - To Abort"
  print "  [+] 1 - Scan status Query"
  print "  [+] 2 - All issuetypes Query"
  print "  [+] 3 - All issues Query"
  print "  [+] 4 - Specific issuetype Query"
  print "  [+] 5 - Specific issue Query"
  print "  [+] 6 - Abort scan"
  print "  [+] 7 - Scan Summary Query"
  print "  [+] 8 - Scan Configuration"
  print "  [+] 9 - Report interface"
  print "  [+] 10 - Pause Scan"
  print "  [+] 11 - Profile Gen"
  
 
  

def query_issuetypes(scan_id, conn, headers):
   conn.request("GET","/api/scan/"+scan_id+"/issuetypes","",headers)
   print_response(conn.getresponse())

def query_issues(scan_id, conn, headers):
   conn.request("GET","/api/scan/"+scan_id+"/issues","",headers)
   print_response(conn.getresponse())

def query_single_issuetype(scan_id, conn, headers):
  issue_type_id = raw_input("Enter issuetypeID: ")
  conn.request("GET","/api/scan/"+scan_id+"/issuetype/" +issue_type_id ,"",headers)
  print_response(conn.getresponse())

def query_single_issue(scan_id, conn, headers):
  issue_id = raw_input("Enter issueID: ")
  conn.request("GET","/api/scan/"+scan_id+"/issue/"+ issue_id,"",headers)
  print_response(conn.getresponse())

def scan_status_query(scan_id, conn, headers):
   conn.request("GET","/api/scan/"+scan_id,"",headers)
   print_response(conn.getresponse())

def scan_summary(scan_id, conn, headers):
   conn.request("GET","/api/scan/"+scan_id+"/summary","",headers)
   print_response(conn.getresponse())

def scan_config(scan_id, conn, headers):
   a = raw_input("Enter Section: ")
   
   conn.request("GET","/api/scan/"+scan_id+"/config?section=" + a,"",headers)
   print_response(conn.getresponse())

def scan_report(scan_id, conn, headers):
  while(True):
    print "  [++] 1 - Display report status"
    print "  [++] 2 - Get Meta data of file[redirect]"
    print "  [++] 3 - Get Meta data of file"
    print "  [++] 4 - Download report"
    print "  [++] 5 - Show report generate reasons"
    print "  [++] 6 - Post report generate trigger"
    print "  [++] 0 - Goto Main Menu"
    a = raw_input("Enter Report Option: ")
    if(int(a) == 1):
      conn.request("GET", "/api/scan/"+scan_id+"/report","",headers)
      print_response(conn.getresponse())
    elif(int(a) == 2):
      report_file_id = raw_input("Report file id: ")
      conn.request("GET", "/api/scan/"+scan_id+"/report/"+report_file_id+"?metainfoonly=true","",headers)
      print_response(conn.getresponse())    
      #conn.request("GET","/scan/"+scan_id+"/report/generate","",headers)
    elif(int(a) == 3):
      report_file_id = raw_input("Report file id: ")
      conn.request("GET", "/api/scan/"+scan_id+"/report/"+report_file_id+"/meta","",headers)
      print_response(conn.getresponse())      
    elif(int(a) == 4):
      report_file_id = raw_input("Report file id: ")
      conn.request("GET", "/api/scan/"+scan_id+"/report/"+report_file_id,"",headers)
      print_response(conn.getresponse(), False)
    elif(int(a) == 5):
      #report_file_id = raw_input("Report file id: ")
      conn.request("GET","/api/scan/"+scan_id+"/report/generate","",headers)
      print_response(conn.getresponse())      
    elif(int(a) == 6):
      #report_file_id = raw_input("Report file id: ")
      content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
      content += "<reason><id>1</id><value>Marked more false positives after report generation</value></reason>"
      conn.request("POST","/scan/"+scan_id+"/report/generate",content,headers)
      print_response(conn.getresponse())    
    else:
      break
   
   
def gen_config(conn, headers):
   a = raw_input("Enter Section: ")   
   conn.request("GET","/api/profile/new?section=" + a,"",headers)
   print_response(conn.getresponse())

def abort_scan(scan_id, conn, headers):
   print "  [++] 1 - Get Abort Query"
   print "  [++] 2 - Post Abort Query"
   a = raw_input("Enter Option: ")
   if(int(a) == 1):
     conn.request("GET","/api/scan/"+scan_id+"/abort","",headers)
     print_response(conn.getresponse())
   else:
     content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
     content += "<reason><id>1</id><value>mistakenly given</value></reason>"
     conn.request("POST","/api/scan/"+scan_id+"/abort",content,headers)
     print_response(conn.getresponse())

def pause_scan(scan_id, conn, headers):
   print "  [++] 1 - Get Pause scan Query"
   print "  [++] 2 - Post Pause scan Query"
   a = raw_input("Enter Option: ")
   if(int(a) == 1):
     conn.request("GET","/api/scan/"+scan_id+"/pause","",headers)
     print_response(conn.getresponse())
   else:
     content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
     content += "<reason><id>1</id><value>scan window</value></reason>"
     conn.request("POST","/api/scan/"+scan_id+"/pause",content,headers)
     print_response(conn.getresponse())

def print_response(response, DATA=True):
   print "[+] - Recieved response: %s %s" % (response.status, response.reason)
   h = response.getheaders()
   print "[+] - Headers"
   for x in range(0,len(h)):
     print "     %s: %s" % (h[x][0],h[x][1])      
   if(DATA):
    print "[+] - Response Data"
    d = response.read()
    print d




if __name__ == '__main__':
  main()




