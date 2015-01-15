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

// Ssu netlet for testing the requestor of the examples/server project

import sorcer.provider.ssu.Ssu

import static sorcer.co.operator.inEnt
import static sorcer.eo.operator.*

Double v1 = 5.0;

task("hello ssu", sig("factorial",
                        Ssu.class,
                        /* Dynamic deployment configuration is an artifact, and we declare that the provider
                         * will be undeployed if idle for 1 minute */
                        deploy(configuration("org.sorcer:ssu:config:5.0"),
                               idle(1))),
     context("ssu", inEnt("arg/x1", v1), result("out/y")));


