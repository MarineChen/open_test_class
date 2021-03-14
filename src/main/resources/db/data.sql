INSERT INTO `role`(`id`, `createdate`, `name`) VALUES (1, NULL, 'admin');
INSERT INTO `role`(`id`, `createdate`, `name`) VALUES (2, NULL, 'teacher');
INSERT INTO `role`(`id`, `createdate`, `name`) VALUES (3, NULL, 'student');

INSERT INTO `user`(`id`, `createdate`, `email`, `name`, `password`, `sex`,  `grade`) VALUES (1, '2021-03-12 09:59:48', 'admin@111.com', 'admin', '0709e06f5eb1347c3894eaea91919226', 1, NULL);
INSERT INTO `user`(`id`, `createdate`, `email`, `name`, `password`, `sex`,  `grade`) VALUES (2, '2021-03-12 09:59:48', 'aa@test.com', '李老师', '0709e06f5eb1347c3894eaea91919226', 1,  NULL);
INSERT INTO `user`(`id`, `createdate`, `email`, `name`, `password`, `sex`,  `grade`) VALUES (3, '2021-03-12 10:33:13', 'b@sdfsdf.com', '同学1', '0709e06f5eb1347c3894eaea91919226', 0,  NULL);
INSERT INTO `user`(`id`, `createdate`, `email`, `name`, `password`, `sex`,  `grade`) VALUES (4, '2021-03-12 17:20:21', 'b@sdfsdf.com', '同学2', '0709e06f5eb1347c3894eaea91919226', 0,  NULL);

INSERT INTO `user_role`(`user_id`, `roles_id`) VALUES (1, 1);
INSERT INTO `user_role`(`user_id`, `roles_id`) VALUES (2, 2);
INSERT INTO `user_role`(`user_id`, `roles_id`) VALUES (3, 3);
INSERT INTO `user_role`(`user_id`, `roles_id`) VALUES (4, 3);

INSERT INTO `lesson`(`id`, `createdate`, `name`, `status`, `description`) VALUES (1, NULL, '思想品德', NULL, '数学课，每周三次');
INSERT INTO `lesson`(`id`, `createdate`, `name`, `status`, `description`) VALUES (2, NULL, '物理', NULL, '数学课，每周三次');
INSERT INTO `lesson`(`id`, `createdate`, `name`, `status`, `description`) VALUES (3, NULL, '数学', 0, '数学课，每周三次');