# Offline architecture proposal

One of the most difficult tasks when doing an application is preparing it to work offline.
In many cases we just cache the data and it is presented as an offline mode, 
but this is not the case we are proposing.

Using only components present in the Android framework we tried to create a showcase
where you can create/delete posts and create/delete comments in an application.

This example also contains an online mode where it just does not cache anything (nor reads
nor writes).

This sample does not implement a presenter patter just because it is simple enough to add
more complexity, but it is perfectly compatible with MVP.

# Loaders and Job scheduler

```Loaders``` are present in the Android SDK since honeycomb but they are not so widely spread 
as other components are. They fit really well with the observer pattern based in notifications
from ```LocalBroadcastManager``` and the job scheduler (or a similar library).

The flow basically goes from an activity to the repository which notifies accordingly
the loader to reload the data. This way the synchronization is easier, as well as
the complete data flow. Please have a look to the code for more details or
the presentation done in the Droidcon Spain '16 ***(Pending video and presentation)***.

Link to presentation (Pending)

Link to video (Pending)

# How to start it?

*Preconditions*: Make sure you have node.js installed and in your command line available
```
cd server
sh start.sh
```

Copy your local IP address into the build.gradle of the application and run the app.

# License
```
Copyright [2016] [Javier de Pedro López]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```