# Creating a Full Web Based Business

_January 2020_



_Author: Michael Mountrakis – mike.mountrakis...AT...gmail.com_



## Abstract

This paper explains the minimal steps on creating a small web-based business. You will find a simple enumeration of the steps involved and an enumeration of all those artifacts that are created during this procedure and from now on will be your company’s assets. Steps are not drilled down to details for simplicity.



## Basic Requirement: company IMSI/MSISDN 
This is the #1 Requirement: A company cell phone.

A cell phone number from the country your business is set up. This must not be your private cell phone. Instead it will be the technical number that every web third party entity refers to your company. This number is the equivalent id just as the VAT in finance.

Make sure it is an easy to remember MSISDN! This is the only requirement. 

This will be referred **myMSISDN**

It is suggested that this number is a prepaid based number. Guard this number with your life – whoever possesses this governs the web part of the company. 



## Site Construction


### Finding the Propriate Domain Name

Step1


You need to choose a web domain name for your business.  For a services company or retailer that does international business you need the following things:



•	Name must be simple catchy and not base country related. Remember the word google does not meant anything in any language but is simple catchy and short.



•	Name must be unique

•	Name must be a single word.

•	Forget .gr, .uk …. .clowm, .cloud, .net..

•	Consider .com, .eu and if you are interested to the local market do also the .gr

Example: google, amazon, apelait, bing…

Go to web whois.com and check the names.  If one of them is taken goto step 1

If and only If you find all three .com .eu and .gr you should consider the name.  


**This will be referred myDOMAIN**

Before buying the 3 domain names myDOMAIN + .com, .eu, .gr goto next section



### External Mails Setup

In order to get the domain from a provider you need to provider an email for correspondence to the IANA. You just don’t want this to be your private mail. For that reason and for several other reasons you need to setup the following mails:



#### Google Mail + Company Account

This is a must: A Google email to manage your business.  Go to this URL

https://accounts.google.com/signup/v2/webcreateaccount?service=mail&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ltmpl=default&gmb=exp&biz=true&flowName=GlifWebSignIn&flowEntry=SignUp

And create a new Google Account for your company.

**This will be referred as myDOMAIN@gmail.com.** 

To finish with the setup put the myMSISDN phone correspondence for Google.

 

#### Outlook Mail + Company Account

This is a must: A Microsoft account to manage your company. Go to this URL

https://outlook.live.com/owa/

And create a new Microsoft Account for your company.

A Google email to manage your business. **This will be referred as myDOMAIN@outlook.com.** 

To finish with the setup put the myMSISDN phone correspondence for Microsoft.


### Choosing the Provider - Buying the Domain

You need to find a provider. Here is the critical decision:

Web Page + Mail Hosting or Virtual Machine hosting?

99% of the cases, you need just a dynamic site, so you normally choose Web Page Hosting + Mail

Second critical decision:

Do I want to speak my language on support or English?

50% of the cases you need to be supported in your native language. For that reason, here are some example options: 

https://www.awardspace.com/   US/UK/Denmark

https://easy.gr/el/  Greek provider with DCs around the globe.

Third Decision:

What will be the technology stack for my page Windows or Linux based?

90% of the cases the choice is the typical Linux/Plesk/MySQL/Wordpress + Mail scheme.

Both providers given are supporting this scheme.

Go to one of the providers and create an account using the myDOMAIN@outlook.com 



### Buying the Domain and Hosting

Just place your domain and host order to the provider. Use your real company details just as your company address, VAT,…. But for the email use:  myDOMAIN@outlook.com



When order will be paid, you will receive in the :  myDOMAIN@outlook.com a mail from your provider giving you the following things, that **you must put in your safe:**

**The email of instructions received in myDOMAIN@outlook.com**

**URL to the Plesk panel**

**Domain Plesk admin username**

**Domain Plesk admin password**

Go to the Plesk panel and update the DNS for your domain. Wait 2-48 hours for IANA DNS updates.



### Creating the Web Mail

Go to the Plesk panel and create a mail account for Word Press administrator. This is NOT your employee account nor has to do anything with your company business. This is where the front-end Wordpress reports problems. Create and store in your safe the following:

**Wordpress Admin mail account:  wpadmin@myDOMAIN.com**

**Wordpress Admin password:  passwd**


### Creating the Web Server Certificates

Go to the Plesk and create an SSL certificate for your domain. For that reason, you may need a third party signed certificate, for example your domain’s certificate might be signed from VeriSIGN. When this step is completed, you need to store in your safe the following things:

**RSA ID: private key file**

**RSA Certificate Sign Request file (.CSR file)**

**Account username for certificate signing authority: login**

**Account password for certificate signing authority: pass**

**Signed Certificate file (.CRT file)**

**Certificate user/password if there is one.  This is your server’s validity!**

The certificate can be tested for password with openSSL tools.

You can do the same with self-signed certificates, but for a Web Selling Site this is not an option…. So you need to create certificates and get them signed from a 3rd party. If you want this to be done on your own, then follow: 

https://github.com/illumine/articles/blob/master/Enable-SSL-for-your-Wordpress-Plesk-site-using-a-free-authority-signed-certificate/Enable-SSL-for-your-Wordpress-Plesk-site-using-a-free-authority-signed-certificate.md



### Build up the site

#### Graphics Part
Ask your graphics designer to do a logo for your **myDOMAIN.com**. 

Along with the logo he should create all the graphics portfolio to support the web site:
From headers, banners, campaigns ... to MS word templates with the company logo.
You must store in your safe:
**Illustrator or Corel files, tiffs, pngs sent by the graphics designer** Make sure you get not only the image files (jpg, png, gif, tif...) but also the artwork files like Adobe Illustrator or Corel Draw or Image Magic or Photoshop or whatever. Do the same for all the else, for example Power Point Templates, Word templates... 



#### Web Part
Go to the Plesk panel, login with domain admin username, domain admin password

That were included in the email from your provider, and setup WordPress. You need to setup WordPress using the Web Server Signed Certificates from the previous step.  

Go and buy a tailor made template for your business accoding tou your company logo and logo colors. Dont be stingly! It is your business!

Make sure that you have full support for the template you bought from the developer.

Make sure that the images used in your articles are yours or sold to you from a proper vendor so that you wont face legal issues in the future from someone who saw his face as a model in a pic served by your site!


Upon successful installation you will create the following account for WordPress administrator that you must store in your safe:

**WordPress admin username: wpadmin - Never use the classic admin**

**WordPress admin password:  password**

**MySQL username: myadmin**

**MySQL password: password**

**Also, the properties.php file from WordPress**

Password: something very strong, minimum 16bytes mix lowercase uppercase, symbols and numbers. EXCLUDE “”,’’, # symbols from passwords.



### Basic SEO

Install Plugin WordPress YOAST SEO PLUGIN and bleed until all your pages are green. A MUST DO.

Also, for each page create some META/KEYWORDS just like this article explains: 
https://illumineconsulting.blogspot.com/2016/03/set-your-html-meta-tags-in-wordpress.html



### Setting Up Employee Emails

Go to Outlook and setup a company account using your real company details for some accounts, for example  d.xioannou@myDOMAIN.com

Go to your Plesk board and configure the SMTP/POP3 with the details given from outlook.

Always use external email than the one offered by the provider. If your domain mail is gone you have to have email to communicate!!! So dont just use the hosting provider email.

Also add the wpadmin@myDOMAIN.com to the Outlook.com mails.

**Must put in your safe box:**

**The login/password to Outolook.com for your business email.** 

Beware those are different from the free email myDOMAIN@outlook.com  



### Setup Redirects

Go to your Plesk board and redirect the domains myDOMAIN.eu myDOMAIN.gr to point to myDOMAIN.com



### Test end to End

Test all your pages with different browsers from a PC Chrome, Edge, Opera, Firefox

Test all your pages with 3 different browsers from an Android, IPhone, WinPhone



### Take your first Backup

Backup the first installation of WordPress and MySQL.  **Put the .tgz archives in your safe.**



### Construction Conclusions

This can cost you only the cost of the domain name. it costed me 24Euros for 2 years. Normally this cost around 3000Euros. In terms of time…. Don’t ask…. Up to this point you have built up your site, but none knows about it….



## Banking Interface

You need:

The company login/password for your company’s web banking

The link in your bank WEB space that your system will call back the bank:

This is the backfire URL from your domain to the bank. This is the forward URL for payments.





## Second Part:  Pushing your Site to Engines and SM



### Search Engines

#### Registering with Google

Use your  myDOMAIN@gmail.com to manage your business:

Register your myDOMAIN.com with  https://analytics.google.com/

•	Google Search Console

•	Google Maps register yourself as the domain manager.

•	Google Analytics register yourself as the domain manager.

•	Add a Google AdSense account to your domain

**Store and keep somewhere safe the Google Site Verification token file. (.html file)**



#### Registering with MS Bing

Use your  myDOMAIN@outlook.com  and myMSISDN to manage your business with Bing:

Register your myDOMAIN.com with Bing Search Services

**Store and keep somewhere safe the Bing Site Verification token file. (.XML file)**







#### Registering with Yadex - Rusia

Use your  myDOMAIN@outlook.com and myMSISDN to manage your business with Yadex:

Register your myDOMAIN.com with Yadex Search Services

**Store and keep somewhere safe the Yadex Site Verification token file. (.html file)**





#### Registering with Baidu - China

Use your  myDOMAIN@outlook.com and myMSISDN to manage your business with Baidu:

Register your myDOMAIN.com with Baidu Search Services

**Store and keep somewhere safe the Baidu Site Verification token file. (.html file)**







### Social Media



#### Facebook

Use your  myDOMAIN@outlook.com and myMSISDN to manage your business with Facebook:

**Store the FB user/password**



#### LinkedIN

Use your  myDOMAIN@outlook.com and myMSISDN to manage your business with LinkedIN:

**Store the LN user/password**



#### Twitter

Use your  myDOMAIN@outlook.com and myMSISDN to manage your business with Twitter:

**Store the Twitter user/password**



