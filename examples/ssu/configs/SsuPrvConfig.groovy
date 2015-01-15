/*
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sorcer.provider.ssu.configs

import org.rioproject.config.Component

import java.util.logging.Level

/**
 * Configuration for the Ssu provider
 *
 * @author Dennis Reedy
 */
@Component('sorcer.core.provider.ServiceProvider')
class SsuProviderConfig {
    /* service provider genetic properties */
    String name = "Ssu"
    String description = "Ssu - bean provider"
    String location = "AFRL/WPAFB"
    boolean spaceEnabled = true
    // remote logging
    boolean remoteLogging=true
    String remoteLoggerManagerName="Logger"
    Level remoteLoggerLevel=Level.FINE;
    String remoteLoggerName="remote.sorcer.provider.ssu.Ssu"
    // local private logging available via by RemoteLogger
    boolean remoteContextLogging = true
    boolean remoteProviderLogging = true
    // persist and reuse service ID
    boolean providerIdPersistent = false
    String iconName="sorcer.jpg";

    // enable monitoring
    // monitorEnabled = true;

    /**
     * We cannot declare this as a property, the class will not be in the classpath when this configuration file is
     * loaded to obtain deployment properties (declared by the SsuDeploymentConfig class below).
     *
     * @return An array of service implementation classes required to load the service
     */
    Class[] getBeanClasses() {
        return [sorcer.provider.ssu.impl.SsuImpl.class]
    }


    Class[] getPublishedInterfaces() {
        return [Thread.currentThread().contextClassLoader.loadClass(interfaceClass)]
    }

    /* This is declared as a static property so the class below can reference it, and used by the published interfaces
     * method above. Removes duplication. */
    static String interfaceClass = "sorcer.provider.ssu.Ssu.class"

}
