# MS Azure AKS Basics


### Connect to your AKS Cluster

1. Optional if you connect using VPN. Get connected with F5 BIG IP to create your VPN connection.


2. Go to URL https://portal.azure.com/ and do the Microsoft phone verification - MFA procedure


3. In MS portal home, go to _All resources_ and find the AKS cluster you need to connect and select 
the _connect_ icon, then click on it.
![click on the _connect_ icon](img/aks_cluster.png "click on the _connect_ icon")


4. This step assumes that you have the _az_ commands installed.
Open your MS-Dos shell and type the following command
```bash
Microsoft Windows [Version 10.0.18363.1082]
(c) 2019 Microsoft Corporation. All rights reserved.

C:\Users\mountrakis>az login
To sign in, use a web browser to open the page https://microsoft.com/devicelogin and enter the code XXXXXXXXX to authenticate.
[
  {
    "cloudName": "AzureCloud",
    "id": "0ea89fa8-210b-xxxx-xxxx-xxxxxxxx",
    "isDefault": false,
```
Do as instructions suggest.

5. From the Connect window in MS Azure portal copy the first connection command:
In the same MS-DOS window type the following command
```bash
C:\Users\mountrakis>az account set --subscription <your subscription id>

C:\Users\mountrakis>az aks get-credentials --resource-group <the resource group your cluster begins> --name <the AKS cluster name>
```

6. Try out some commands
```bash
C:\Users\c5179796>kubectl get nodes
NAME                               STATUS   ROLES   AGE   VERSION
aks-wpaasdev-19766648-vmss000000   Ready    agent   35d   v1.17.9
aks-wpaasdev-19766648-vmss000001   Ready    agent   35d   v1.17.9
aks-wpaasdev-19766648-vmss000002   Ready    agent   35d   v1.17.9
```

Tryout some commands from the basic Kubernetes cheat sheet:
https://kubernetes.io/docs/reference/kubectl/cheatsheet/