#
# Copyright 2016 LinkedIn Corp.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.
#

# --- !Ups

ALTER TABLE yarn_app_heuristic_result_details
    DROP PRIMARY KEY,
    DROP FOREIGN KEY yarn_app_heuristic_result_details_f1;

ALTER TABLE yarn_app_heuristic_result_details
    ADD id INT NOT NULL AUTO_INCREMENT COMMENT 'The application heuristic result details id' FIRST,
    ADD PRIMARY KEY (id),
    ADD CONSTRAINT yarn_app_heuristic_result_details_f1 FOREIGN KEY (yarn_app_heuristic_result_id) REFERENCES yarn_app_heuristic_result (id);

# --- !Downs


