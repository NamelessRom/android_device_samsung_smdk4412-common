/*
 * Copyright (C) 2016 The NamelessROM Project
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

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

	extern void _ZN7android16MediaBufferGroup14acquire_bufferEPPNS_11MediaBufferEb(void **buffer, int nonBlocking);

	//status_t acquire_buffer(MediaBuffer **buffer);
	void _ZN7android16MediaBufferGroup14acquire_bufferEPPNS_11MediaBufferE(void **buffer);

void _ZN7android16MediaBufferGroup14acquire_bufferEPPNS_11MediaBufferE(void **buffer) {
	return _ZN7android16MediaBufferGroup14acquire_bufferEPPNS_11MediaBufferEb(buffer, 0);
}
