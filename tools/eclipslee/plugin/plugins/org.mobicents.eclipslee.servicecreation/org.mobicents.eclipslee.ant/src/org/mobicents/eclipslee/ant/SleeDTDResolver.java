
/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.mobicents.eclipslee.ant;

import org.mobicents.eclipslee.util.DTDResolver;

public class SleeDTDResolver extends DTDResolver {
    public SleeDTDResolver() {
        super();
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN", "slee/dtd/slee-deployable-unit_1_0.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN", "slee/dtd/slee-sbb-jar_1_0.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN", "slee/dtd/slee-profile-spec-jar_1_0.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN", "slee/dtd/slee-event-jar_1_0.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN", "slee/dtd/slee-resource-adaptor-type-jar_1_0.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN", "slee/dtd/slee-resource-adaptor-jar_1_0.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN", "slee/dtd/slee-service-xml_1_0.dtd");
        
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN", "slee/dtd/slee-event-jar_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN", "slee/dtd/slee-profile-spec-jar_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN", "slee/dtd/slee-sbb-jar_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.1//EN", "slee/dtd/slee-deployable-unit_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN", "slee/dtd/slee-resource-adaptor-type-jar_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN", "slee/dtd/slee-resource-adaptor-jar_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN", "slee/dtd/slee-service-xml_1_1.dtd");
        registerDTDResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Library 1.1//EN", "slee/dtd/slee-library-jar_1_1.dtd");

    }
}
