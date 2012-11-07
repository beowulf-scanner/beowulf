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
	scan_report(scan_id, conn, headers)
      elif(int(a) == 3):
	abort_scan(scan_id, conn, headers)
      elif(int(a) == 4):
	scan_summary(scan_id, conn, headers)
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
  print "  [+] 2 - Report interface"
  print "  [+] 3 - Abort scan"
  print "  [+] 4 - Scan Summary Query"
 
def scan_summary(scan_id, conn, headers):
   conn.request("GET","/api/scan/"+scan_id+"/summary","",headers)
   print_response(conn.getresponse())

def scan_status_query(scan_id, conn, headers):
   conn.request("GET","/api/scan/"+scan_id,"",headers)
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
      conn.request("POST","/api/scan/"+scan_id+"/report/generate",content,headers)
      print_response(conn.getresponse())    
    else:
      break
   
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




